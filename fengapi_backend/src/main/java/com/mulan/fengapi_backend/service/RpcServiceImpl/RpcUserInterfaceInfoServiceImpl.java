package com.mulan.fengapi_backend.service.RpcServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    @Override
    public boolean invokeCountUpdate(Long userId, Long interfaceInfoId) {
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("totalNum = totalNum + 1 and leftNum = leftNum - 1")
                .eq("userId",userId)
                .eq("interfaceInfoId",interfaceInfoId);
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




