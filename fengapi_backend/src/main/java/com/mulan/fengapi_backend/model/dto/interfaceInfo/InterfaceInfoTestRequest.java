package com.mulan.fengapi_backend.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 测试调用接口请求
 * @author wwwwind
 */
@Data
public class InterfaceInfoTestRequest implements Serializable {
    private static final long serialVersionUID = -4256598515888254670L;
    /**
     * id
     */
    private Long id;

    /**
     * 测试请求示例
     */
    private String requestExample;
}
