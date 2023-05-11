package com.mulan.fengapi_backend.service.RpcServiceImpl;

import com.mulan.fengapi_backend.model.entity.InterfaceRoute;
import com.mulan.fengapi_backend.service.InterfaceRouteService;
import com.mulan.fengapi_backend.util.InterfaceRouteUtils;
import com.mulan.fengapi_common.model.entity.GatewayRoute;
import com.mulan.fengapi_common.service.RpcGatewayRouteService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
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
        List<GatewayRoute> gatewayRoutes = new ArrayList<>();
        for (InterfaceRoute interfaceRoute : allRoute) {
            // 只有路由状态为上线才添加
            if (interfaceRoute.getStatus() == 1) {
                GatewayRoute gatewayRoute = InterfaceRouteUtils.genGatewayRoute(interfaceRoute);
                gatewayRoutes.add(gatewayRoute);
            }
        }
        System.out.println("数据库路由解析成功，数据：" + gatewayRoutes.size());
        return gatewayRoutes;
    }
}
