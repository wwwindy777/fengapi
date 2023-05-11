package com.mulan.fengapi_backend.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mulan.fengapi_backend.common.ErrorCode;
import com.mulan.fengapi_backend.exception.BusinessException;
import com.mulan.fengapi_backend.mapper.InterfaceInfoMapper;
import com.mulan.fengapi_backend.mapper.InterfaceRouteMapper;
import com.mulan.fengapi_backend.model.VO.InterfaceInfoVO;
import com.mulan.fengapi_backend.model.entity.InterfaceInfo;
import com.mulan.fengapi_backend.model.entity.InterfaceRoute;
import com.mulan.fengapi_backend.model.enums.InterfaceInfoStatusEnum;
import com.mulan.fengapi_backend.service.InterfaceInfoService;
import com.mulan.fengapi_backend.util.InterfaceRouteUtils;
import com.mulan.fengapi_backend.util.TestInterfaceUtils;
import com.mulan.fengapi_common.model.entity.GatewayRoute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.mulan.fengapi_backend.constant.CommonConstant.API_GATEWAY_URL;

/**
 * @author wwwwind
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2023-03-22 15:30:36
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Resource
    InterfaceRouteMapper interfaceRouteMapper;

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
        return API_GATEWAY_URL + "api/" + interfaceName;
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
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean onlineInterfaceInfo(Long id) {
        // 判断是否存在该接口
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 数据库中如果没有该接口路由需要先添加路由
        QueryWrapper<InterfaceRoute> routeQueryWrapper = new QueryWrapper<>();
        routeQueryWrapper.eq("routeId", oldInterfaceInfo.getName());
        InterfaceRoute route = interfaceRouteMapper.selectOne(routeQueryWrapper);
        if (route == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "路由不存在");
        }
        // 生成网关路由
        GatewayRoute gatewayRoute = InterfaceRouteUtils.genGatewayRoute(route);
        String routeStr = JSONUtil.toJsonStr(gatewayRoute);
        HttpResponse response = HttpRequest.post(API_GATEWAY_URL + "route/add").body(routeStr).execute();
        if (!response.isOk()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "生成网关路由失败");
        }
        // 判断该接口是否可以调用
        if (!TestInterfaceUtils.isAvailableInterface(oldInterfaceInfo)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "测试接口调用失败");
        }
        // 修改路由状态
        InterfaceRoute updateRoute = new InterfaceRoute();
        updateRoute.setId(route.getId());
        updateRoute.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        int updateRouteRes = interfaceRouteMapper.updateById(updateRoute);
        if (updateRouteRes <= 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新路由状态失败");
        }
        // 修改状态
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        return this.updateById(interfaceInfo);
    }

    /**
     * 下线接口
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean offlineInterfaceInfo(Long id) {
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 如果接口已经是下线状态报错
        if (oldInterfaceInfo.getStatus().equals(0)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已下线");
        }
        // 删除网关路由
        String name = oldInterfaceInfo.getName();
        HttpResponse response = HttpRequest.post(API_GATEWAY_URL + "route/routes/" + name).execute();
        if (!response.isOk()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除网关路由失败");
        }
        // 修改状态
        UpdateWrapper<InterfaceRoute> routeUpdateWrapper = new UpdateWrapper<>();
        routeUpdateWrapper.eq("routeId",name).setSql("status = 0");
        interfaceRouteMapper.update(null,routeUpdateWrapper);
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        return this.updateById(interfaceInfo);
    }
}




