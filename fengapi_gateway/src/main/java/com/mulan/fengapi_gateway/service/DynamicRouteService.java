package com.mulan.fengapi_gateway.service;

import com.mulan.fengapi_common.model.entity.GatewayRoute;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

/**
 * @author wwwwind
 */
public interface DynamicRouteService {
    String add(GatewayRoute definition);
    String update(GatewayRoute definition);
    Mono<ResponseEntity<Object>> delete(String id);
}
