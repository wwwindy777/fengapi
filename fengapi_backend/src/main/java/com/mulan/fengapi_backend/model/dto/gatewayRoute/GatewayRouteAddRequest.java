package com.mulan.fengapi_backend.model.dto.gatewayRoute;

import com.mulan.fengapi_common.model.entity.GatewayRoute;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 添加接口请求
 * @author wwwwind
 */
@Data
public class GatewayRouteAddRequest implements Serializable {
    private static final long serialVersionUID = -4256598515888254670L;
    /**
     * 路由Id，与接口名称一致
     */
    private String routeId;

    /**
     * 接口地址
     */
    private String uri;

    /**
     * 断言
     */
    private List<GatewayRoute.GatewayPredicateDefinition> predicates;

    /**
     * 过滤器
     */
    private List<GatewayRoute.GatewayFilterDefinition> filters;

    /**
     * order
     */
    private Integer routeOrder;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;
}
