package com.mulan.fengapi_backend.model.VO;

import com.mulan.fengapi_common.model.entity.GatewayRoute;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Gateway路由
 * @author wwwwind
 */
@Data
public class GatewayRouteAddVO implements Serializable {
    private Long dbId;
    private String id;
    private List<GatewayRoute.GatewayPredicateDefinition> predicates;
    private List<GatewayRoute.GatewayFilterDefinition> filters;
    private String uri;
    private Integer order;
}