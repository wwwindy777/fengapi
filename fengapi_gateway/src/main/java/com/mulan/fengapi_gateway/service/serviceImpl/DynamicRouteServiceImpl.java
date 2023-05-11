package com.mulan.fengapi_gateway.service.serviceImpl;

import com.mulan.fengapi_common.model.entity.GatewayRoute;
import com.mulan.fengapi_gateway.service.DynamicRouteService;
import com.mulan.fengapi_gateway.util.GatewayRouteUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware, DynamicRouteService {

	@Resource
	private RouteDefinitionWriter routeDefinitionWriter;
	private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		// TODO Auto-generated method stub
		this.applicationEventPublisher = applicationEventPublisher;
	}

	//增加路由
	@Override
	public String add(GatewayRoute definition) {
		RouteDefinition routeDefinition = GatewayRouteUtils.assembleRouteDefinition(definition);
		routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
		this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
		return "success";
	}

	//更新路由
	@Override
	public String update(GatewayRoute definition) {
		try {
			delete(definition.getId());
		} catch (Exception e) {
			return "update fail, not find route routeId: " + definition.getId();
		}
		try {
			RouteDefinition routeDefinition = GatewayRouteUtils.assembleRouteDefinition(definition);
			routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
			this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
			return "success";
		} catch (Exception e) {
			return "update route fail";
		}

	}

	//删除路由
	@Override
	public String delete(String id) {
		this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
		this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
		return "delete success";
	}
}