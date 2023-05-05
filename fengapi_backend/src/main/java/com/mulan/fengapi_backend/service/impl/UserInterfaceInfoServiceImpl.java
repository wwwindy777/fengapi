package com.mulan.fengapi_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mulan.fengapi_backend.mapper.UserInterfaceInfoMapper;
import com.mulan.fengapi_backend.model.entity.UserInterfaceInfo;
import com.mulan.fengapi_backend.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

/**
* @author wwwwind
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2023-03-30 09:19:36
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    /**
     * 更新接口调用次数
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    @Override
    public boolean invokeCountUpdate(Long userId,Long interfaceInfoId) {
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("totalNum = totalNum + 1 and leftNum = leftNum - 1")
                .eq("userId",userId)
                .eq("interfaceInfoId",interfaceInfoId);
        return this.update(updateWrapper);
    }
}




