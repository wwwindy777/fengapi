package com.mulan.fengapi_backend.common;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.mulan.fengapi_backend.model.entity.InterfaceInfo;
import com.mulan.fengapi_backend.model.enums.InterfaceMethodEnum;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试接口工具
 *
 * @author wwwwind
 */
public class TestInterfaceUtils {

    public static boolean isAvailableInterface(InterfaceInfo interfaceInfo) {
        String url = interfaceInfo.getUrl();
        String requestExample = interfaceInfo.getRequestExample();
        Integer method = interfaceInfo.getMethod();
        //解析请求示例
        Gson gson = new Gson();
        JsonElement jsonElement = JsonParser.parseString(requestExample);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        //判断请求类型，只要有get就使用get
        if (!method.equals(InterfaceMethodEnum.POST.ordinal())){
            //取出请求示例中的params参数
            JsonObject params = jsonObject.get("params").getAsJsonObject();
            //没有参数直接发请求
            if (params.size() == 0){
                HttpResponse response = HttpRequest.get(url).execute();
                return response.getStatus() == 200;
            }
            //有参数将参数拼接
            Type type = new TypeToken<HashMap<String, JsonElement>>() {
            }.getType();
            Map<String,JsonElement> paramsMap = gson.fromJson(params, type);
            //将参数Map转为字符串
            String paramsStr = HttpUtil.toParams(paramsMap);
            HttpResponse response = HttpRequest.get(url + paramsStr).execute();
            return response.getStatus() == 200;
        } else {
            //取出请求示例中的body参数
            JsonObject body = jsonObject.get("body").getAsJsonObject();
            HttpResponse response = HttpRequest.post(url).body(body.toString()).execute();
            System.out.println(response.body());
            return response.getStatus() == 200;
        }
    }
}
