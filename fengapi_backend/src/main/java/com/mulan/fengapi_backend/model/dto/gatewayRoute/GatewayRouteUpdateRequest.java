package com.mulan.fengapi_backend.model.dto.gatewayRoute;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新接口请求
 * @author wwwwind
 */
@Data
public class GatewayRouteUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1652488653826777330L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 路由Id，与接口名称一致
     */
    private String routeId;

    /**
     * 接口地址
     */
    private String uri;

    /**
     * order
     */
    private Integer routeOrder;

    /**
     * 断言
     */
    private String predicates;

    /**
     * 过滤器
     */
    private String filters;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;
}
