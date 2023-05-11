package com.mulan.fengapi_backend.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mulan.fengapi_backend.model.entity.InterfaceRoute;
import com.mulan.fengapi_common.model.entity.GatewayRoute;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wwwwind
 */
public class InterfaceRouteUtils {
    /**
     * 把数据库的路由信息转为网关路由实体
     * @param interfaceRoute
     * @return
     */
    public static GatewayRoute genGatewayRoute(InterfaceRoute interfaceRoute){
        Gson gson = new Gson();
        GatewayRoute gatewayRoute = new GatewayRoute();
        gatewayRoute.setId(interfaceRoute.getRouteId());
        gatewayRoute.setOrder(interfaceRoute.getRouteOrder());
        gatewayRoute.setUri(interfaceRoute.getUri());
        Type predicatesType = new TypeToken<ArrayList<GatewayRoute.GatewayPredicateDefinition>>() {
        }.getType();
        List<GatewayRoute.GatewayPredicateDefinition> predicates = gson.fromJson(interfaceRoute.getPredicates(), predicatesType);
        gatewayRoute.setPredicates(predicates);
        Type filtersType = new TypeToken<ArrayList<GatewayRoute.GatewayFilterDefinition>>() {
        }.getType();
        List<GatewayRoute.GatewayFilterDefinition> filters = gson.fromJson(interfaceRoute.getFilters(), filtersType);
        gatewayRoute.setFilters(filters);
        return gatewayRoute;
    }
}
