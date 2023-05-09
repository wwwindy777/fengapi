package com.mulan.fengapi_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mulan.fengapi_backend.common.ErrorCode;
import com.mulan.fengapi_backend.common.TestInterfaceUtils;
import com.mulan.fengapi_backend.constant.CommonConstant;
import com.mulan.fengapi_backend.exception.BusinessException;
import com.mulan.fengapi_backend.mapper.InterfaceInfoMapper;
import com.mulan.fengapi_backend.model.VO.InterfaceInfoVO;
import com.mulan.fengapi_backend.model.entity.InterfaceInfo;
import com.mulan.fengapi_backend.model.enums.InterfaceInfoStatusEnum;
import com.mulan.fengapi_backend.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author wwwwind
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2023-03-22 15:30:36
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    /**
     * 请求参数较验
     *
     * @param interfaceInfo 实体
     * @param isAdd         是否是添加操作
     */
    @Override
    public void verifyInterfaceInfo(InterfaceInfo interfaceInfo, boolean isAdd) {
        //以下参数不能为空
        Integer method = interfaceInfo.getMethod();
        String name = interfaceInfo.getName();
        String url = interfaceInfo.getUrl();
        String requestHeader = interfaceInfo.getRequestHeader();
        String requestParams = interfaceInfo.getRequestParams();
        String responseHeader = interfaceInfo.getResponseHeader();
        String requestExample = interfaceInfo.getRequestExample();
        if (method > 2 || method < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isAnyBlank(name, url, requestHeader, requestParams, responseHeader, requestExample)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //描述长度不能大于50
        if (interfaceInfo.getDescription().length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述过长");
        }
    }

    /**
     * 处理原始接口url
     *
     * @param interfaceName
     * @return
     */
    @Override
    public String mapUrl(String interfaceName) {
        return CommonConstant.API_GATEWAY_URL + interfaceName;
    }

    /**
     * 获取接口VO
     *
     * @param info
     * @return
     */
    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo info) {
        if (info == null) {
            return null;
        }
        InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        BeanUtils.copyProperties(info, interfaceInfoVO);
        return interfaceInfoVO;
    }

    /**
     * 上线接口
     * @param id
     * @return
     */
    @Override
    public boolean onlineInterfaceInfo(Long id) {
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 判断该接口是否可以调用
        if (!TestInterfaceUtils.isAvailableInterface(oldInterfaceInfo)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"接口不可用");
        }
        // 修改状态
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        return this.updateById(interfaceInfo);
    }
}




