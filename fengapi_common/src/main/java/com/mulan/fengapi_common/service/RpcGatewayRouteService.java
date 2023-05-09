package com.mulan.fengapi_common.service;

import com.mulan.fengapi_common.model.entity.GatewayRoute;

import java.util.List;

/**
 * @author wwwwind
 */
public interface RpcGatewayRouteService {
    List<GatewayRoute> getAllRoute();
}
