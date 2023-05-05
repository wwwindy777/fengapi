package com.mulan.fengapiclient;

import com.mulan.fengapiclient.client.FengApiClient;

import java.util.HashMap;

/**
 * 接口调用测试
 * @author wwwwind
 */
public class Test {
    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        FengApiClient fengApiClient = new FengApiClient("9c6f7a8eef571db85ad454350c074589", "9ce298d5758fce251abc87fdab7a0b21");
        String url = "http://localhost:8666/test/post";
        HashMap<String, Object> body = new HashMap<>();
        body.put("text", "clientTest");
        //String res = fengApiClient.postApiRequest("getUser", body);
        String res = fengApiClient.getApiRequest("getText",body);

        System.out.println(res);
        long l1 = System.currentTimeMillis();
        System.out.println("调用接口耗时：" + (l1-l));
    }
}
