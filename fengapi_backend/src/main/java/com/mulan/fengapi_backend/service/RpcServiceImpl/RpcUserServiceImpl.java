package com.mulan.fengapi_backend.service.RpcServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mulan.fengapi_backend.service.UserService;
import com.mulan.fengapi_common.model.entity.User;
import com.mulan.fengapi_common.service.RpcUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

/**
 * @author wwwwind
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-03-24 22:10:50
 */
@DubboService
@Slf4j
public class RpcUserServiceImpl implements RpcUserService {

    @Resource
    private UserService userService;

    @Override
    public User getUserInfoById(Long id) {
        com.mulan.fengapi_backend.model.entity.User byId = userService.getById(id);
        if (byId == null) {
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(byId, user);
        return user;
    }

    @Override
    public User getUserInfoByAk(String accessKey) {
        QueryWrapper<com.mulan.fengapi_backend.model.entity.User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("accessKey", accessKey);
        com.mulan.fengapi_backend.model.entity.User one = userService.getOne(userQueryWrapper);
        if (one == null) {
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(one, user);
        return user;
    }
}




