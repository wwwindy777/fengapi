package com.mulan.fengapi_backend.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 接口路由信息
 * @TableName interface_route
 */
@TableName(value ="interface_route")
@Data
public class InterfaceRoute implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 路由Id，与接口名称一致
     */
    private String routeId;

    /**
     * 接口地址
     */
    private String uri;

    /**
     * order
     */
    private Integer routeOrder;

    /**
     * 断言
     */
    private String predicates;

    /**
     * 过滤器
     */
    private String filters;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}