package com.mulan.fengapi_backend.service.RpcServiceImpl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mulan.fengapi_backend.model.entity.InterfaceRoute;
import com.mulan.fengapi_backend.service.InterfaceRouteService;
import com.mulan.fengapi_common.model.entity.GatewayRoute;
import com.mulan.fengapi_common.service.RpcGatewayRouteService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wwwwind
 */
@DubboService
public class RpcGatewayRouteServiceImpl implements RpcGatewayRouteService {
    @Resource
    InterfaceRouteService interfaceRouteService;
    @Override
    public List<GatewayRoute> getAllRoute() {
        //1 查询所有路由信息
        List<InterfaceRoute> allRoute = interfaceRouteService.list();
        //2 将数据库中的字段映射成路由实体
        Gson gson = new Gson();
        List<GatewayRoute> gatewayRoutes = new ArrayList<>();
        for (InterfaceRoute interfaceRoute : allRoute) {
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
            gatewayRoutes.add(gatewayRoute);
        }
        System.out.println("数据库路由解析成功，数据：" + gatewayRoutes.size());
        return gatewayRoutes;
    }
}
