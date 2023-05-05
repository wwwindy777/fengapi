package com.mulan.fengapi_backend.service.RpcServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mulan.fengapi_backend.service.InterfaceInfoService;
import com.mulan.fengapi_common.model.entity.InterfaceInfo;
import com.mulan.fengapi_common.service.RpcInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

/**
 * @author wwwwind
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2023-03-22 15:30:36
 */
@DubboService
public class RpcInterfaceInfoServiceImpl implements RpcInterfaceInfoService {
    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Override
    public InterfaceInfo getInterfaceInfoByName(String name) {
        QueryWrapper<com.mulan.fengapi_backend.model.entity.InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        com.mulan.fengapi_backend.model.entity.InterfaceInfo byName = interfaceInfoService.getOne(queryWrapper);
        if (byName == null){
            return null;
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(byName,interfaceInfo);
        return interfaceInfo;
    }
}




