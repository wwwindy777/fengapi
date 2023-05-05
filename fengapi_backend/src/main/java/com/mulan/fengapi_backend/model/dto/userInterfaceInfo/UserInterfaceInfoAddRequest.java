package com.mulan.fengapi_backend.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 添加请求
 * @author wwwwind
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {
    private static final long serialVersionUID = -8681491558878911974L;
    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;
}
