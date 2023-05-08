package com.mulan.fengapi_gateway.filter;

import cn.hutool.json.JSONUtil;
import com.mulan.fengapi_common.model.entity.InterfaceInfo;
import com.mulan.fengapi_common.model.entity.User;
import com.mulan.fengapi_common.model.entity.UserInterfaceInfo;
import com.mulan.fengapi_common.service.RpcInterfaceInfoService;
import com.mulan.fengapi_common.service.RpcUserInterfaceInfoService;
import com.mulan.fengapi_common.service.RpcUserService;
import com.mulan.fengapiclient.client.FengApiClient;
import com.mulan.fengapiclient.util.RequestUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.entity.ContentType;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    private static final String NONCE_KEY = "fengapi:gateway:nonce:";

    @Resource
    private FengApiClient fengApiClient;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @DubboReference
    private RpcInterfaceInfoService rpcInterfaceInfoService;
    @DubboReference
    private RpcUserService rpcUserService;
    @DubboReference
    private RpcUserInterfaceInfoService rpcUserInterfaceInfoService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 取出请求头
        HttpHeaders headers = request.getHeaders();
        String interfaceName = headers.getFirst("interfaceName");
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        log.info(accessKey + " " + nonce + " " + timestamp + " " + sign);
        //1 请求头不能空
        if (StringUtils.isAnyBlank(nonce, timestamp, interfaceName, sign, accessKey)) {
            return handleNoAuth(response);
        }
        //2 接口状态校验
        assert interfaceName != null;
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = rpcInterfaceInfoService.getInterfaceInfoByName(interfaceName);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
        Long interfaceIdLong = interfaceInfo.getId();
        //3 用户调用接口权限校验
        User userInfo = null;
        try {
            userInfo = rpcUserService.getUserInfoByAk(accessKey);
        } catch (Exception e) {
            log.error("getUserInfo error", e);
        }
        if (userInfo == null) {
            return handleNoAuth(response);
        }
        Long userId = userInfo.getId();
        UserInterfaceInfo userInterfaceInfo = null;
        try {
            userInterfaceInfo = rpcUserInterfaceInfoService.getUserInterfaceInfo(userId, interfaceIdLong);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (userInterfaceInfo == null || userInterfaceInfo.getStatus().equals(1) || userInterfaceInfo.getLeftNum() <= 0) {
            return handleNoAuth(response);
        }
        // 保存用户接口关系的id，用于后面响应成功后更新调用次数
        HashMap<String, String> map = new HashMap<>();
        map.put("userInterfaceInfoId", userInterfaceInfo.getId().toString());
        ServerWebExchangeUtils.putUriTemplateVariables(exchange,map);
        //4 较验请求头
        String secretKey = userInfo.getSecretKey();
        //4.1 时间戳不能超过5min
        assert timestamp != null;
        if (System.currentTimeMillis() / 1000 - Long.parseLong(timestamp) > 300) {
            return handleNoAuth(response);
        }
        //4.2 nonce必须不存在,要从数据库查
        assert nonce != null;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(NONCE_KEY + nonce))) {
            return handleNoAuth(response);
        }
        //4.3 存nonce设置过期时间
        stringRedisTemplate.opsForValue().set(NONCE_KEY + nonce, "", 300000);
        //4.4 验证签名
        // 因为验证签名要取到body，gateway取body操作比较复杂且取完后需要做一系列操作
        // 所以验证完签名后该过滤器就要返回，处理response只能交给下个过滤器做，目前还没发现其他方法
        String method = request.getMethodValue();
        if (HttpMethod.POST.name().equalsIgnoreCase(method)) {
            ModifyRequestBodyGatewayFilterFactory.Config modifyRequestConfig = new ModifyRequestBodyGatewayFilterFactory.Config()
                    .setContentType(ContentType.APPLICATION_JSON.getMimeType())
                    .setRewriteFunction(Map.class, Map.class, (exchange1, originalRequestBody) -> {
                        String bodyStr = JSONUtil.toJsonStr(originalRequestBody);
                        String userSign = RequestUtils.generateSign(bodyStr, secretKey);
                        if (!userSign.equals(sign)) {
                            throw new RuntimeException("签名验证失败");
                        }
                        return Mono.just(originalRequestBody);
                    });

            return new ModifyRequestBodyGatewayFilterFactory().apply(modifyRequestConfig).filter(exchange, chain);
        }

        if (HttpMethod.GET.name().equalsIgnoreCase(method)) {
            URI uri = request.getURI();
            String userSign = RequestUtils.generateSign(uri.toString(), secretKey);
            if (!userSign.equals(sign)) {
                throw new RuntimeException("签名验证失败");
            }
            return chain.filter(exchange.mutate().request(request).build());
        }
        return chain.filter(exchange);
    }
    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
    @Override
    public int getOrder() {
        return 0;
    }

}