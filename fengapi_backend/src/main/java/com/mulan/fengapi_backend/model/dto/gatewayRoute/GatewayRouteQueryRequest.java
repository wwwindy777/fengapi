package com.mulan.fengapi_backend.model.dto.gatewayRoute;

import com.mulan.fengapi_backend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 * @author wwwwind
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GatewayRouteQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -6482106868436423091L;
    /**
     * 路由Id，与接口名称一致
     */
    private String routeId;
}