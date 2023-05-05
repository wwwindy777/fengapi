package com.mulan.fengapi_backend;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Gson解析多层json
 * @author wwwwind
 */
public class JsonTest {
    public static void main(String[] args) {
        Gson gson = new Gson();
        String jsonStr1 = "{\"code\":0,\"message\":\"ok\",\"description\":\"\",\"data\":{}}";
        String jsonStr = "{\"params\":{\"text\":\"fengshuai\"},\"body\":{\"userName\":\"fengfengaaaa\"}}";
        //利用JsonParser得到jsonElement解析树，jsonObject可以通过json中的键访问值
        JsonElement jsonElement = JsonParser.parseString(jsonStr);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement data = jsonObject.get("body");
        System.out.println(data.isJsonNull());
        System.out.println(data.isJsonObject());
        System.out.println(data.getAsJsonObject().size());
        //jsonElement也可作为gson.fromJson的第一个参数
        Type type1 = new TypeToken<HashMap<String, String>>() {
        }.getType();
        Map<String,String> aa = gson.fromJson(data, type1);
        System.out.println(aa);
        //转Map时JsonElement可以作为value用于解析多层json
        Type type = new TypeToken<HashMap<String, JsonElement>>() {
        }.getType();
        Map<String,JsonElement> o = gson.fromJson(jsonStr, type);
        System.out.println(o);
    }
}
