package com.mulan.fengapi_backend.service;

import com.mulan.fengapi_backend.model.entity.InterfaceRoute;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author wwwwind
* @description 针对表【interface_route(接口路由信息)】的数据库操作Service
* @createDate 2023-05-08 14:54:32
*/
public interface InterfaceRouteService extends IService<InterfaceRoute> {

    void verifyGatewayRoute(InterfaceRoute interfaceRoute, boolean isAdd);
}
