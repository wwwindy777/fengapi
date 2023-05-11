package com.mulan.fengapi_gateway.util;

import com.mulan.fengapi_common.model.entity.GatewayRoute;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wwwwind
 */
public class GatewayRouteUtils {
    //把传递进来的参数转换成路由对象
    public static RouteDefinition assembleRouteDefinition(GatewayRoute gwdefinition) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(gwdefinition.getId());
        definition.setOrder(gwdefinition.getOrder());

        //设置断言
        List<PredicateDefinition> pdList=new ArrayList<>();
        List<GatewayRoute.GatewayPredicateDefinition> gatewayPredicateDefinitionList=gwdefinition.getPredicates();
        if (gatewayPredicateDefinitionList != null){
            for (GatewayRoute.GatewayPredicateDefinition gpDefinition : gatewayPredicateDefinitionList) {
                PredicateDefinition predicate = new PredicateDefinition();
                predicate.setArgs(gpDefinition.getArgs());
                predicate.setName(gpDefinition.getName());
                pdList.add(predicate);
            }
            definition.setPredicates(pdList);
        }
        //设置过滤器
        List<FilterDefinition> filters = new ArrayList<>();
        List<GatewayRoute.GatewayFilterDefinition> gatewayFilters = gwdefinition.getFilters();
        if(gatewayFilters != null){
            for(GatewayRoute.GatewayFilterDefinition filterDefinition : gatewayFilters){
                FilterDefinition filter = new FilterDefinition();
                filter.setName(filterDefinition.getName());
                filter.setArgs(filterDefinition.getArgs());
                filters.add(filter);
            }
            definition.setFilters(filters);
        }
        URI uri;
        if (gwdefinition.getUri().startsWith("http")) {
            uri = UriComponentsBuilder.fromHttpUrl(gwdefinition.getUri()).build().toUri();
        } else {
            // uri为 lb://consumer-service 时使用下面的方法
            uri = URI.create(gwdefinition.getUri());
        }
        definition.setUri(uri);
        return definition;
    }
}
