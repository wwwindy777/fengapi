package com.mulan.fengapi_backend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 单id请求
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}