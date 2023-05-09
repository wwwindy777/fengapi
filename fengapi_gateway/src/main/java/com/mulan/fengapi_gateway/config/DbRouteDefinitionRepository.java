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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DbRouteDefinitionRepository implements RouteDefinitionRepository, ApplicationEventPublisherAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbRouteDefinitionRepository.class);
    private ApplicationEventPublisher publisher;
    private final List<RouteDefinition> routeDefinitionList = new ArrayList<>();
    @DubboReference
    RpcGatewayRouteService rpcGatewayRouteService;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostConstruct
    public void init() {
        load();
    }

    ///**
    // * 监听事件刷新配置
    // */
    //@EventListener
    //public void listenEvent(RouteConfigRefreshEvent event) {
    //    load();
    //    this.publisher.publishEvent(new RefreshRoutesEvent(this));
    //}

    /**
     * 加载
     */
    private void load() {
        List<GatewayRoute> allRoute = null;
        try {
            allRoute = rpcGatewayRouteService.getAllRoute();
            LOGGER.info("获取路由成功，路由条数：" + allRoute.size());
        } catch (Exception e) {
            LOGGER.error("加载路由失败", e);
        }
        if (allRoute == null) {
            LOGGER.info("数据库无路由信息");
            return;
        }
        allRoute.forEach(route -> routeDefinitionList.add(GatewayRouteUtils.assembleRouteDefinition(route)));
        LOGGER.info("路由配置已加载,加载条数: " + routeDefinitionList.size());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return Mono.defer(() -> Mono.error(new NotFoundException("Unsupported operation")));
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return Mono.defer(() -> Mono.error(new NotFoundException("Unsupported operation")));
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routeDefinitionList);
    }
}