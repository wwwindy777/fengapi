package com.mulan.fengapi_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mulan.fengapi_backend.annotation.AuthCheck;
import com.mulan.fengapi_backend.common.BaseResponse;
import com.mulan.fengapi_backend.common.DeleteRequest;
import com.mulan.fengapi_backend.common.ErrorCode;
import com.mulan.fengapi_backend.common.ResultUtils;
import com.mulan.fengapi_backend.constant.UserConstant;
import com.mulan.fengapi_backend.exception.BusinessException;
import com.mulan.fengapi_backend.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.mulan.fengapi_backend.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.mulan.fengapi_backend.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.mulan.fengapi_backend.model.entity.UserInterfaceInfo;
import com.mulan.fengapi_backend.service.UserInterfaceInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 信息相关
 * @author wwwwind
 */
@RestController
@RequestMapping("userInterfaceInfo/")
public class UserInterfaceInfoController {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 添加
     *
     * @param addRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addRequest(@RequestBody UserInterfaceInfoAddRequest addRequest) {
        if (addRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(addRequest, userInterfaceInfo);
        userInterfaceInfoService.save(userInterfaceInfo);
        return ResultUtils.success(userInterfaceInfo.getId());
    }

    /**
     * 删除信息
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
        //判断该信息是否存在
        UserInterfaceInfo byId = userInterfaceInfoService.getById(deleteRequest.getId());
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "删除的信息不存在");
        }
        boolean res = userInterfaceInfoService.removeById(deleteRequest.getId());
        if (!res) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 修改信息
     *
     * @param updateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateRequest(@RequestBody UserInterfaceInfoUpdateRequest updateRequest) {
        if (updateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(updateRequest, userInterfaceInfo);
        //查询该条信息是否存在
        UserInterfaceInfo byId = userInterfaceInfoService.getById(userInterfaceInfo.getId());
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean res = userInterfaceInfoService.updateById(userInterfaceInfo);
        if (!res) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 获取列表
     *
     * @param queryRequest
     * @return
     */
    @GetMapping("/searchList")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    //TODO：增加分页查询
    public BaseResponse<List<UserInterfaceInfo>> getUserInterfaceInfoList(UserInterfaceInfoQueryRequest queryRequest) {
        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        if (queryRequest != null) {
            BeanUtils.copyProperties(queryRequest, userInterfaceInfoQuery);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoService.list(queryWrapper);
        return ResultUtils.success(userInterfaceInfoList);
    }

}
