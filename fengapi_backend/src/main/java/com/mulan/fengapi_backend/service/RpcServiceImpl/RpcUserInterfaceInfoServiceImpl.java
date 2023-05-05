package com.mulan.fengapi_backend.service.RpcServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mulan.fengapi_backend.common.ErrorCode;
import com.mulan.fengapi_backend.exception.BusinessException;
import com.mulan.fengapi_backend.model.entity.UserInterfaceInfo;
import com.mulan.fengapi_backend.service.UserInterfaceInfoService;
import com.mulan.fengapi_common.service.RpcUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

/**
* @author wwwwind
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-03-30 09:19:36
*/
@DubboService
public class RpcUserInterfaceInfoServiceImpl implements RpcUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    /**
     * 更新接口调用次数
     * @param userInterfaceInfoId
     * @return
     */
    @Override
    public boolean invokeCountUpdate(Long userInterfaceInfoId) {
        if (userInterfaceInfoId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userInterfaceInfoId);
        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return userInterfaceInfoService.update(updateWrapper);
    }

    @Override
    public com.mulan.fengapi_common.model.entity.UserInterfaceInfo getUserInterfaceInfo(Long userId, Long interfaceInfoId) {
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId)
                .eq("interfaceInfoId",interfaceInfoId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getOne(queryWrapper);
        com.mulan.fengapi_common.model.entity.UserInterfaceInfo userInterfaceInfoReturn = new com.mulan.fengapi_common.model.entity.UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfo,userInterfaceInfoReturn);
        return userInterfaceInfoReturn;
    }
}




