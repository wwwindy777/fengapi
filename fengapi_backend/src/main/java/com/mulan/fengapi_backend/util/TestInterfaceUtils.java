package com.mulan.fengapi_backend.util;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mulan.fengapi_backend.common.ErrorCode;
import com.mulan.fengapi_backend.exception.BusinessException;
import com.mulan.fengapi_backend.model.entity.InterfaceInfo;
import com.mulan.fengapi_backend.model.enums.InterfaceMethodEnum;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static com.mulan.fengapi_backend.constant.CommonConstant.API_GATEWAY_URL;

/**
 * 测试接口工具
 *
 * @author wwwwind
 */
public class TestInterfaceUtils {

    public static boolean isAvailableInterface(InterfaceInfo interfaceInfo) {
        String requestExample = interfaceInfo.getRequestExample();
        String res = doInvokeInterfaceTest(interfaceInfo, requestExample);
        return res != null;
    }
    public static String doInvokeInterfaceTest(InterfaceInfo interfaceInfo,String requestExample){
        String url = API_GATEWAY_URL + "api/" + interfaceInfo.getName();
        Integer method = interfaceInfo.getMethod();
        //解析请求示例
        Gson gson = new Gson();
        JsonObject params;
        JsonObject body;
        try {
            JsonElement jsonElement = JsonParser.parseString(requestExample);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            //取出请求示例中的params参数
            params = jsonObject.get("params").getAsJsonObject();
            //取出请求示例中的body参数
            body = jsonObject.get("body").getAsJsonObject();
        } catch (Exception e){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"解析请求参数异常");
        }
        //添加请头跳过网关鉴权
        HashMap<String, String> headMap = new HashMap<>();
        headMap.put("mulan_pass", "fengapi_master");
        //判断请求类型，只要有get就使用get
        if (!method.equals(InterfaceMethodEnum.POST.ordinal())) {
            //没有参数直接发请求
            if (params.size() == 0) {
                HttpResponse response = HttpRequest.get(url).addHeaders(headMap).execute();
                if (response.getStatus() == 200){
                    return response.body();
                }
            }
            //有参数将参数拼接
            Type type = new TypeToken<HashMap<String, JsonElement>>() {
            }.getType();
            Map<String, JsonElement> paramsMap = gson.fromJson(params, type);
            //将参数Map转为字符串
            String paramsStr = HttpUtil.toParams(paramsMap);
            HttpResponse response = HttpRequest.get(url + "?" + paramsStr).addHeaders(headMap).execute();
            if (response.getStatus() == 200){
                return response.body();
            }
        } else {
            HttpResponse response = HttpRequest.post(url).body(body.toString()).addHeaders(headMap).execute();
            if (response.getStatus() == 200){
                return response.body();
            }
        }
        return null;
    }
}
