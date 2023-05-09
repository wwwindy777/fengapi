package com.mulan.fengapi_backend.model.dto.gatewayRoute;

import lombok.Data;

import java.io.Serializable;

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
    private String predicates;

    /**
     * 过滤器
     */
    private String filters;
}
