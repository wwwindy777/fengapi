package com.mulan.fengapiclient.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.mulan.fengapiclient.util.RequestUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class FengApiClient implements ApiClient{
    private static final String GATEWAY_URL = "http://localhost:8666/api/";
    private String accessKey;
    private String secretKey;

    public FengApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * get请求
     * @param interfaceName 接口名
     * @param params 请求参数
     * @return
     */
    @Override
    public String getApiRequest(String interfaceName,Map<String, Object> params) {
        String url = getUrl(interfaceName, params);
        HttpResponse response = HttpRequest.get(url)
                .addHeaders(getHeaderMap(url,interfaceName))
                .execute();
        log.info(response.body());
        if (response.getStatus() == 200){
            return response.body();
        }
        return null;
    }

    /**
     * post请求
     *
     * @param interfaceName 接口名
     * @param body 请求体Map
     * @return 响应json字符串
     */
    @Override
    public String postApiRequest(String interfaceName,Map<String, Object> body) {
        String url = getUrl(interfaceName, null);
        String bodyStr = JSONUtil.toJsonStr(body);
        HttpResponse response = HttpRequest.post(url)
                .addHeaders(getHeaderMap(bodyStr,interfaceName))
                .body(bodyStr).execute();
        log.info(response.body());
        if (response.getStatus() == 200){
            return response.body();
        }
        return null;
    }

    /**
     * 生成请求头
     * @param request 请求参数/请求体
     * @param interfaceName 接口名
     * @return
     */
    private Map<String, String> getHeaderMap(String request,String interfaceName) {
        HashMap<String, String> header = new HashMap<>();
        header.put("interfaceName",interfaceName);
        header.put("accessKey", accessKey);
        header.put("nonce", RandomUtil.randomNumbers(5));
        header.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
        header.put("sign", RequestUtils.generateSign(request,secretKey));
        return header;
    }

    /**
     * 获取完整请求地址
     * @param interfaceName 接口名称
     * @param params 请求参数
     * @return 请求地址
     */
    private String getUrl(String interfaceName, Map<String, Object> params){
        if (params == null){
            return GATEWAY_URL + interfaceName;
        }
        String paramsStr = HttpUtil.toParams(params);
        return GATEWAY_URL + interfaceName + "?" + paramsStr;
    }
}
