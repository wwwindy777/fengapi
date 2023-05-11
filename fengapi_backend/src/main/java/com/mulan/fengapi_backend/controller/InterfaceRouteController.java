package com.mulan.fengapi_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mulan.fengapi_backend.annotation.AuthCheck;
import com.mulan.fengapi_backend.common.BaseResponse;
import com.mulan.fengapi_backend.common.DeleteRequest;
import com.mulan.fengapi_backend.common.ErrorCode;
import com.mulan.fengapi_backend.common.ResultUtils;
import com.mulan.fengapi_backend.constant.UserConstant;
import com.mulan.fengapi_backend.exception.BusinessException;
import com.mulan.fengapi_backend.model.dto.gatewayRoute.GatewayRouteAddRequest;
import com.mulan.fengapi_backend.model.dto.gatewayRoute.GatewayRouteQueryRequest;
import com.mulan.fengapi_backend.model.dto.gatewayRoute.GatewayRouteUpdateRequest;
import com.mulan.fengapi_backend.model.entity.InterfaceRoute;
import com.mulan.fengapi_backend.service.InterfaceRouteService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author wwwwind
 */
@RestController
@RequestMapping("/gateway/route")
public class InterfaceRouteController {
    @Resource
    private InterfaceRouteService interfaceRouteService;

    /**
     * 添加路由信息
     *
     * @param addRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addRequest(@RequestBody GatewayRouteAddRequest addRequest) {
        if (addRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceRoute interfaceRoute = interfaceRouteService.genInterfaceRoute(addRequest);
        interfaceRouteService.save(interfaceRoute);
        return ResultUtils.success(interfaceRoute.getId());
    }

    /**
     * 删除路由信息
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteRequest(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断该路由是否存在
        InterfaceRoute byId = interfaceRouteService.getById(deleteRequest.getId());
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "删除的路由不存在");
        }
        boolean res = interfaceRouteService.removeById(deleteRequest.getId());
        if (!res) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 修改路由信息
     *
     * @param updateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateRequest(@RequestBody GatewayRouteUpdateRequest updateRequest) {
        if (updateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断id是否合法
        if (updateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id小于0");
        }
        //路由是否存在
        InterfaceRoute byId = interfaceRouteService.getById(updateRequest.getId());
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //生成更新对象
        InterfaceRoute interfaceRoute = interfaceRouteService.genInterfaceRoute(updateRequest, byId);
        boolean res = interfaceRouteService.updateById(interfaceRoute);
        if (!res) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 获取路由列表
     *
     * @param queryRequest
     * @return
     */
    @GetMapping("/searchList")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<InterfaceRoute>> getRouteList(GatewayRouteQueryRequest queryRequest) {
        InterfaceRoute interfaceRoute = new InterfaceRoute();
        if (queryRequest != null) {
            BeanUtils.copyProperties(queryRequest, interfaceRoute);
        }
        assert queryRequest != null;
        long current = queryRequest.getCurrentPage();
        long size = queryRequest.getPageSize();
        QueryWrapper<InterfaceRoute> queryWrapper = new QueryWrapper<>(interfaceRoute);
        Page<InterfaceRoute> page = interfaceRouteService.page(new Page<>(current,size), queryWrapper);
        return ResultUtils.success(page);
    }

}
