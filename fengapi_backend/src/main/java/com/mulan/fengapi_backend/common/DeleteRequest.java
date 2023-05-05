package com.mulan.fengapi_backend.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = -8085460585503107567L;
    /**
     * 主键
     */
    private Long id;
}
