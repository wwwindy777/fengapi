package com.mulan.fengapi_backend.service;

import com.mulan.fengapi_backend.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author wwwwind
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2023-03-30 09:19:36
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    public boolean invokeCountUpdate(Long userId,Long interfaceInfoId);
}
