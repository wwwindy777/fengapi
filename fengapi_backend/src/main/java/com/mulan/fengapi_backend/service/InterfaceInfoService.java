package com.mulan.fengapi_backend.service;

import com.mulan.fengapi_backend.model.VO.InterfaceInfoVO;
import com.mulan.fengapi_backend.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author wwwwind
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-03-22 15:30:36
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void verifyInterfaceInfo(InterfaceInfo interfaceInfo,boolean isAdd);
    String mapUrl(String interfaceName);
    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo info);
    boolean onlineInterfaceInfo(Long id);
    boolean offlineInterfaceInfo(Long id);
}
