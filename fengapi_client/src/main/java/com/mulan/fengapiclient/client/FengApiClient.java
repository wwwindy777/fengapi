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
    private String accessKey;
    private String secretKey;

    public FengApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * get请求
     * @param url 请求地址
     * @param params 请求参数
     * @return
     */
    @Override
    public String getApiRequest(String url, String interfaceId,Map<String, Object> params) {
        String paramsStr = HttpUtil.toParams(params);
        HttpResponse response = HttpRequest.get(url+paramsStr)
                .addHeaders(getHeaderMap(params.toString(),interfaceId))
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
     * @param url 请求地址
     * @param body 请求体Map
     * @return 响应json字符串
     */
    @Override
    public String postApiRequest(String url, String interfaceId,Map<String, Object> body) {
        String bodyStr = JSONUtil.toJsonStr(body);
        HttpResponse response = HttpRequest.post(url)
                .addHeaders(getHeaderMap(bodyStr,interfaceId))
                .body(bodyStr).execute();
        log.info(response.body());
        if (response.getStatus() == 200){
            return response.body();
        }
        return null;
    }

    //获取请求头map
    private Map<String, String> getHeaderMap(String request,String interfaceId) {
        HashMap<String, String> header = new HashMap<>();
        header.put("interfaceId",interfaceId);
        header.put("accessKey", accessKey);
        header.put("nonce", RandomUtil.randomNumbers(5));
        header.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
        header.put("sign", RequestUtils.generateSign(request,secretKey));
        return header;
    }
}
