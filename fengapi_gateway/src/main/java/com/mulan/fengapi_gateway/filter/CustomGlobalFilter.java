package com.mulan.fengapi_gateway.filter;

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
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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
        log.info(request.getId());
        log.info(request.getMethodValue());
        log.info("请求方式：" + request.getMethod());
        log.info("请求来源：" + request.getRemoteAddress());
        //1 请求校验
        HttpHeaders headers = request.getHeaders();
        String interfaceId = headers.getFirst("interfaceId");
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        System.out.println(accessKey + " " + nonce + " " + timestamp + " " + sign);
        //1.1 请求头不能空
        if (StringUtils.isAnyBlank(nonce, timestamp, interfaceId, sign, accessKey)) {
            return handleNoAuth(response);
        }
        //1.2 较验ak，从数据库中查
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
        String userName = userInfo.getUserName();
        String userAccount = userInfo.getUserAccount();
        String userRole = userInfo.getUserRole();
        String secretKey = userInfo.getSecretKey();
        //1.3 时间戳不能超过5min
        assert timestamp != null;
        if (System.currentTimeMillis() / 1000 - Long.parseLong(timestamp) > 300) {
            return handleNoAuth(response);
        }
        //1.4 nonce必须不存在,要从数据库查
        assert nonce != null;
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(NONCE_KEY + nonce))) {
            return handleNoAuth(response);
        }
        //1.5 存nonce设置过期时间
        stringRedisTemplate.opsForValue().set(NONCE_KEY + nonce, "", 300000);
        //1.6 验证签名
        String getParams = request.getQueryParams().toString();
        String body = null;
        String requestContent = Objects.requireNonNull(request.getMethod()).matches("get") ? getParams : body;
        String userSign = RequestUtils.generateSign(requestContent, secretKey);
        //if (!userSign.equals(sign)) {
        //    return handleNoAuth(response);
        //}
        //2 接口状态校验
        assert interfaceId != null;
        long interfaceIdLong = Long.parseLong(interfaceId);
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = rpcInterfaceInfoService.getInterfaceInfoById(interfaceIdLong);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
        //3 用户调用接口权限校验
        UserInterfaceInfo userInterfaceInfo = null;
        try {
            userInterfaceInfo = rpcUserInterfaceInfoService.getUserInterfaceInfo(userId, interfaceIdLong);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (userInterfaceInfo == null || userInterfaceInfo.getStatus().equals(1) || userInterfaceInfo.getLeftNum() <= 0) {
            return handleNoAuth(response);
        }
        //4 调用接口
        return handleResponse(exchange, chain, interfaceIdLong, userId);
    }

    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    private Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 5. 调用成功，接口调用次数 + 1 invokeCount
                                        try {
                                            rpcUserInterfaceInfoService.invokeCountUpdate(userId, interfaceInfoId);
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 6. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    private Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }

}