package com.mulan.fengapi_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mulan.fengapi_backend.common.ErrorCode;
import com.mulan.fengapi_backend.exception.BusinessException;
import com.mulan.fengapi_backend.mapper.InterfaceRouteMapper;
import com.mulan.fengapi_backend.model.entity.InterfaceRoute;
import com.mulan.fengapi_backend.service.InterfaceRouteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author wwwwind
 * @description 针对表【interface_route(接口路由信息)】的数据库操作Service实现
 * @createDate 2023-05-08 14:54:32
 */
@Service
public class InterfaceRouteServiceImpl extends ServiceImpl<InterfaceRouteMapper, InterfaceRoute>
        implements InterfaceRouteService {

    @Override
    public void verifyGatewayRoute(InterfaceRoute interfaceRoute, boolean isAdd) {
        //以下参数不能为空
        String routeId = interfaceRoute.getRouteId();
        String uri = interfaceRoute.getUri();
        String predicates = interfaceRoute.getPredicates();
        if (StringUtils.isAnyBlank(routeId, uri, predicates)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }
}




