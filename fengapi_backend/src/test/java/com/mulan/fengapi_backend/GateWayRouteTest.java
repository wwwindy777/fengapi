package com.mulan.fengapi_backend;

import com.google.gson.Gson;
import com.mulan.fengapi_common.model.entity.GatewayRoute;

/**
 * 创建路由测试
 * @author wwwwind
 */
public class GateWayRouteTest {
    public static void main(String[] args) {
        String routeDemo = "{\"filters\":[{\"args\":{\"name\":\"hystrix\",\"fallbackUri\":\"forward:/hystrix\"},\"name\":\"Hystrix\"},{\"args\":{},\"name\":\"RateLimit\"}],\"id\":\"DEMO_API_ROUTE\",\"order\":1,\"predicates\":[{\"args\":{\"_genkey_0\":\"/demoApi/**\"},\"name\":\"Path\"}],\"uri\":\"lb://demo-api\"}";
        Gson gson = new Gson();
        GatewayRoute gatewayRoute = gson.fromJson(routeDemo, GatewayRoute.class);
        String s = gson.toJson(gatewayRoute);
        System.out.println(s);
        System.out.println(routeDemo.equals(s));
    }
}
