package com.mulan.fengapi_gateway.config;

import com.mulan.fengapi_common.model.entity.GatewayRoute;
import com.mulan.fengapi_common.service.RpcGatewayRouteService;
import com.mulan.fengapi_gateway.util.GatewayRouteUtils;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 从数据库加载路由
 */
@Component
public class DbRouteDefinitionRepository implements RouteDefinitionRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbRouteDefinitionRepository.class);
    public  final Map<String, RouteDefinition> routes = Collections.synchronizedMap(new LinkedHashMap<>());
    @DubboReference
    RpcGatewayRouteService rpcGatewayRouteService;

    @PostConstruct
    public void init() {
        load();
    }

    /**
     * 加载路由
     */
    private void load() {
        List<GatewayRoute> allRoute = null;
        try {
            allRoute = rpcGatewayRouteService.getAllRoute();
            LOGGER.info("获取路由成功，路由条数：" + allRoute.size());
        } catch (Exception e) {
            LOGGER.error("加载路由失败", e);
        }
        if (allRoute == null || allRoute.size() == 0) {
            LOGGER.info("数据库无路由信息");
            return;
        }
        allRoute.forEach(route -> routes.put(route.getId(),GatewayRouteUtils.assembleRouteDefinition(route)));
        LOGGER.info("路由配置已加载,加载条数: " + routes.size());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap( r -> {
            routes.put(r.getId(), r);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (routes.containsKey(id)) {
                routes.remove(id);
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition not found: "+routeId)));
        });
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        Collection<RouteDefinition> values = routes.values();
        return Flux.fromIterable(values);
    }
}