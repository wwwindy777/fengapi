package com.mulan.fengapi_common.service;

import com.mulan.fengapi_common.model.entity.User;

public interface RpcUserService {
    User getUserInfoById(Long id);
    User getUserInfoByAk(String accessKey);
}
