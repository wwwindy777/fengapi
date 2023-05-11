package com.mulan.fengapi_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mulan.fengapi_backend.annotation.AuthCheck;
import com.mulan.fengapi_backend.common.*;
import com.mulan.fengapi_backend.constant.UserConstant;
import com.mulan.fengapi_backend.exception.BusinessException;
import com.mulan.fengapi_backend.model.VO.InterfaceInfoVO;
import com.mulan.fengapi_backend.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.mulan.fengapi_backend.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.mulan.fengapi_backend.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.mulan.fengapi_backend.model.entity.InterfaceInfo;
import com.mulan.fengapi_backend.model.entity.User;
import com.mulan.fengapi_backend.model.enums.InterfaceInfoStatusEnum;
import com.mulan.fengapi_backend.service.InterfaceInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口信息相关
 * @author wwwwind
 */
@RestController
@RequestMapping("/interfaceInfo")
public class InterfaceInfoController {
    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 添加接口
     *
     * @param addRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addRequest(@RequestBody InterfaceInfoAddRequest addRequest, HttpServletRequest request) {
        if (addRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        Long userId = loginUser.getId();
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(addRequest, interfaceInfo);
        //参数较验
        interfaceInfoService.verifyInterfaceInfo(interfaceInfo, true);
        interfaceInfo.setUserId(userId);
        interfaceInfoService.save(interfaceInfo);
        return ResultUtils.success(interfaceInfo.getId());
    }

    /**
     * 删除接口信息
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
        //判断该接口信息是否存在
        InterfaceInfo byId = interfaceInfoService.getById(deleteRequest.getId());
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "删除的接口不存在");
        }
        boolean res = interfaceInfoService.removeById(deleteRequest.getId());
        if (!res) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     * 修改接口信息
     *
     * @param updateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateRequest(@RequestBody InterfaceInfoUpdateRequest updateRequest) {
        if (updateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断id是否合法
        if (updateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id小于0");
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(updateRequest, interfaceInfo);
        //参数较验
        interfaceInfoService.verifyInterfaceInfo(interfaceInfo, false);
        //判断该接口信息是否存在
        InterfaceInfo byId = interfaceInfoService.getById(interfaceInfo.getId());
        if (byId == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean res = interfaceInfoService.updateById(interfaceInfo);
        if (!res) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新失败");
        }
        return ResultUtils.success(true);
    }
    /**
     * 根据id获取接口信息
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(Integer id) {
        if (id == null || id < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo byId = interfaceInfoService.getById(id);
        if (byId == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(byId);
    }

    /**
     * 获取接口列表（后台）
     *
     * @param queryRequest
     * @return
     */
    @GetMapping("/searchList")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<InterfaceInfo>> getInterfaceInfoList(InterfaceInfoQueryRequest queryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (queryRequest != null) {
            BeanUtils.copyProperties(queryRequest, interfaceInfoQuery);
        }
        assert queryRequest != null;
        long current = queryRequest.getCurrentPage();
        long size = queryRequest.getPageSize();
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        Page<InterfaceInfo> page = interfaceInfoService.page(new Page<>(current,size), queryWrapper);
        return ResultUtils.success(page);
    }
    /**
     * 获取接口列表（前台）
     *
     * @param queryRequest
     * @return
     */
    @GetMapping("/searchList/vo")
    //TODO：1. 增加分页查询
    public BaseResponse<List<InterfaceInfoVO>> getInterfaceInfoVOList(InterfaceInfoQueryRequest queryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (queryRequest != null) {
            BeanUtils.copyProperties(queryRequest, interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        //1. 不展示下线的接口
        List<InterfaceInfoVO> interfaceInfoVOS = interfaceInfoList.stream().filter(i -> i.getStatus().equals(1))
                .map(info -> interfaceInfoService.getInterfaceInfoVO(info)).collect(Collectors.toList());
        //2. 接口地址改为经过API网关
        interfaceInfoVOS.forEach(infoVO -> infoVO.setUrl(interfaceInfoService.mapUrl(infoVO.getName())));
        return ResultUtils.success(interfaceInfoVOS);
    }

    /**
     * 上线接口
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        boolean result = interfaceInfoService.onlineInterfaceInfo(id);
        return ResultUtils.success(result);
    }

    /**
     * 下线
     *
     * @param idRequest
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = idRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 如果接口已经是下线状态报错
        if (oldInterfaceInfo.getStatus().equals(0)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口已下线");
        }
        // 仅管理员可修改
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }
    //测试调用

}
