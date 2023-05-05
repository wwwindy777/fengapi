package com.mulan.fengapi_backend.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求
 * @author wwwwind
 */
//@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest implements Serializable {

    private static final long serialVersionUID = -6482106868436423091L;
    /**
     * 主键
     */
    private Long id;

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
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private Integer method;

    /**
     * 创建人
     */
    private Long userId;

}