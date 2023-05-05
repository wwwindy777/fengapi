package com.mulan.fengapi_backend.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询接口请求
 * @author wwwwind
 */
@Data
public class InterfaceInfoSearchRequest implements Serializable {
    private static final long serialVersionUID = -4227576137616766896L;
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
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

}
