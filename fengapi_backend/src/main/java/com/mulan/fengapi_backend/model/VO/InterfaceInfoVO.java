package com.mulan.fengapi_backend.model.VO;


import lombok.Data;

import java.io.Serializable;

/**
 * 前台接口信息视图
 *
 * @author mulan
 */
@Data
public class InterfaceInfoVO implements Serializable {
    private static final long serialVersionUID = 65763285509098742L;
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
