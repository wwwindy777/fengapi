package com.mulan.fengapi_backend.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 添加接口请求
 * @author wwwwind
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {
    private static final long serialVersionUID = -4256598515888254670L;
    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 请求示例
     */
    private String requestExample;

    /**
     * 请求类型
     */
    private Integer method;

}
