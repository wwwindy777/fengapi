package com.mulan.fengapi_backend;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import cn.hutool.json.JSONUtil;

import com.google.gson.Gson;
import com.mulan.fengapi_backend.model.entity.GateWayRoute;

/**
 * 创建路由测试
 * @author wwwwind
 */
public class GateWayRouteTest {
    public static void main(String[] args) {
        LinkedHashMap<String, String> stringStringLinkedHashMap = new LinkedHashMap<>();
        stringStringLinkedHashMap.put("_genkey_0", "/first");
        GateWayRoute.Predicate predicate = new GateWayRoute.Predicate("Path",stringStringLinkedHashMap);
        ArrayList<GateWayRoute.Predicate> predicates = new ArrayList<>();
        predicates.add(predicate);
        GateWayRoute gateWayRoute = new GateWayRoute();
        gateWayRoute.setId("route");
        gateWayRoute.setPredicates(predicates);
        gateWayRoute.setUrl("https://www.uri-destination.org");
        gateWayRoute.setOrder(0);
        Gson gson = new Gson();
        String gs = gson.toJson(gateWayRoute, gateWayRoute.getClass());
        System.out.println(gs);
        JSONUtil.toJsonStr(gateWayRoute);
        System.out.println(gateWayRoute);
    }
}
