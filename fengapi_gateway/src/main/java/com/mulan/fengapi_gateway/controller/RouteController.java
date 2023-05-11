package com.mulan.fengapi_gateway.controller;

import com.mulan.fengapi_common.model.entity.GatewayRoute;
import com.mulan.fengapi_gateway.service.DynamicRouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 动态路由服务
 */
@RestController
@RequestMapping("/route")
public class RouteController {

	@Resource
	private DynamicRouteService dynamicRouteService;
	
    /**
     * 添加路由
     **/
	@PostMapping("/add")
	public String add(@RequestBody GatewayRoute gatewayDefinition) {
		System.out.println("add = " + gatewayDefinition.toString());
		String flag = "fail";
		try {
			flag = this.dynamicRouteService.add(gatewayDefinition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
    /**
     * 删除路由
     **/
	@PostMapping("/routes/{id}")
	public String delete(@PathVariable String id) {
		try {
			return this.dynamicRouteService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
    /**
     * 修改路由
     **/
	@PostMapping("/update")
	public String update(@RequestBody GatewayRoute gatewayDefinition) {
		System.out.println("update = " + gatewayDefinition.toString());
		return this.dynamicRouteService.update(gatewayDefinition);
	}
}