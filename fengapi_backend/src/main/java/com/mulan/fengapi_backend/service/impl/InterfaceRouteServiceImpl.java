package com.mulan.fengapi_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mulan.fengapi_backend.common.ErrorCode;
import com.mulan.fengapi_backend.exception.BusinessException;
import com.mulan.fengapi_backend.mapper.InterfaceInfoMapper;
import com.mulan.fengapi_backend.mapper.InterfaceRouteMapper;
import com.mulan.fengapi_backend.model.dto.gatewayRoute.GatewayRouteAddRequest;
import com.mulan.fengapi_backend.model.dto.gatewayRoute.GatewayRouteUpdateRequest;
import com.mulan.fengapi_backend.model.entity.InterfaceInfo;
import com.mulan.fengapi_backend.model.entity.InterfaceRoute;
import com.mulan.fengapi_backend.service.InterfaceRouteService;
import com.mulan.fengapi_common.model.entity.GatewayRoute.GatewayFilterDefinition;
import com.mulan.fengapi_common.model.entity.GatewayRoute.GatewayPredicateDefinition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/**
 * @author wwwwind
 * @description 针对表【interface_route(接口路由信息)】的数据库操作Service实现
 * @createDate 2023-05-08 14:54:32
 */
@Service
public class InterfaceRouteServiceImpl extends ServiceImpl<InterfaceRouteMapper, InterfaceRoute>
        implements InterfaceRouteService {

    @Resource
    InterfaceInfoMapper interfaceInfoMapper;
    /**
     * 把添加路由请求转化为数据库对象
     *
     * @param addRequest
     * @return
     */
    @Override
    public InterfaceRoute genInterfaceRoute(GatewayRouteAddRequest addRequest) {
        String routeId = addRequest.getRouteId();
        String uri = addRequest.getUri();
        List<GatewayPredicateDefinition> predicates = addRequest.getPredicates();
        List<GatewayFilterDefinition> filters = addRequest.getFilters();
        Integer routeOrder = addRequest.getRouteOrder();
        Integer status = addRequest.getStatus();
        //路由id，uri不能为空，断言至少一个
        if (StringUtils.isAnyBlank(routeId, uri) || predicates.size() == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "路由ID，uri，断言不能为空");
        }
        //接口必须存在
        QueryWrapper<InterfaceInfo> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("name", routeId);
        InterfaceInfo one = interfaceInfoMapper.selectOne(infoQueryWrapper);
        if (one == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在");
        }
        //将请求参数赋值给数据库对象
        InterfaceRoute interfaceRoute = new InterfaceRoute();
        Gson gson = new Gson();
        interfaceRoute.setRouteId(routeId);
        interfaceRoute.setUri(uri);
        interfaceRoute.setRouteOrder(routeOrder);
        interfaceRoute.setStatus(status);
        Type predicateType = new TypeToken<List<GatewayPredicateDefinition>>() {
        }.getType();
        String predicateStr = gson.toJson(predicates, predicateType);
        interfaceRoute.setPredicates(predicateStr);
        if (filters.size() > 0) {
            Type filterType = new TypeToken<List<GatewayFilterDefinition>>() {
            }.getType();
            String filterStr = gson.toJson(filters, filterType);
            interfaceRoute.setFilters(filterStr);
        } else {
            //给每个接口添加默认的限流过滤器
            String defaultRateLimiter = "[{\"name\":\"RequestRateLimiter\",\"args\":{\"key-resolver\":\"#{@ipKeyResolver}\",\"redis-rate-limiter.replenishRate\":1,\"redis-rate-limiter.burstCapacity\":1,\"redis-rate-limiter.requestedTokens\":1}}]";
            interfaceRoute.setFilters(defaultRateLimiter);
        }
        return interfaceRoute;
    }

    /**
     * 把更新路由请求生成更新对象
     *
     * @param updateRequest
     * @param oldRoute
     * @return
     */
    @Override
    public InterfaceRoute genInterfaceRoute(GatewayRouteUpdateRequest updateRequest, InterfaceRoute oldRoute) {
        Long id = updateRequest.getId();
        String routeId = updateRequest.getRouteId();
        String uri = updateRequest.getUri();
        Integer routeOrder = updateRequest.getRouteOrder();
        List<GatewayPredicateDefinition> predicates = updateRequest.getPredicates();
        List<GatewayFilterDefinition> filters = updateRequest.getFilters();
        Integer status = updateRequest.getStatus();
        //如果某条数据和之前的不一样，就赋值给新对象
        InterfaceRoute interfaceRoute = new InterfaceRoute();
        interfaceRoute.setId(id);
        if (StringUtils.isNotBlank(routeId) && !routeId.equals(oldRoute.getRouteId())) {
            interfaceRoute.setRouteId(routeId);
        }
        if (StringUtils.isNotBlank(uri) && !uri.equals(oldRoute.getUri())) {
            interfaceRoute.setRouteId(routeId);
        }
        if (!Objects.equals(routeOrder, oldRoute.getRouteOrder())) {
            interfaceRoute.setRouteOrder(routeOrder);
        }
        if (!Objects.equals(status, oldRoute.getStatus())) {
            interfaceRoute.setStatus(status);
        }
        Gson gson = new Gson();
        if (predicates.size() > 0) {
            Type predicateType = new TypeToken<List<GatewayPredicateDefinition>>() {
            }.getType();
            String predicateStr = gson.toJson(predicates, predicateType);
            if (!predicateStr.equals(oldRoute.getPredicates())) {
                interfaceRoute.setPredicates(predicateStr);
            }
        }
        if (filters.size() > 0) {
            Type filterType = new TypeToken<List<GatewayFilterDefinition>>() {
            }.getType();
            String filterStr = gson.toJson(filters, filterType);
            if (!filterStr.equals(oldRoute.getFilters())) {
                interfaceRoute.setPredicates(filterStr);
            }
        }
        return interfaceRoute;
    }
}




