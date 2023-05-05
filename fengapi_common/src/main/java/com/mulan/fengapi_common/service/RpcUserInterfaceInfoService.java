package com.mulan.fengapi_common.service;

import com.mulan.fengapi_common.model.entity.UserInterfaceInfo;

public interface RpcUserInterfaceInfoService {
    boolean invokeCountUpdate(Long userInterfaceInfoId);
    UserInterfaceInfo getUserInterfaceInfo(Long userId, Long interfaceInfoId);
}
