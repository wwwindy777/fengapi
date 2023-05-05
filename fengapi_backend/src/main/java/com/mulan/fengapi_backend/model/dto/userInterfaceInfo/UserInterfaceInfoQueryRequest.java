package com.mulan.fengapi_backend.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询请求
 * @author wwwwind
 */
//@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceInfoQueryRequest implements Serializable {
    private static final long serialVersionUID = 6890216027009743323L;
    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;
}