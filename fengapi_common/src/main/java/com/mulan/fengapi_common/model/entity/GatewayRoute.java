package com.mulan.fengapi_common.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Gateway路由
 * @author wwwwind
 */
@Data
public class GatewayRoute implements Serializable {
    private String id;
    private List<GatewayPredicateDefinition> predicates;
    private List<GatewayFilterDefinition> filters;
    private String uri;
    private Integer order;
    @Data
    public static class GatewayPredicateDefinition implements Serializable{
        public GatewayPredicateDefinition(String name, LinkedHashMap<String, String> args) {
            this.name = name;
            this.args = args;
        }
        private String name;
        private Map<String,String> args;
    }
    @Data
    public static class GatewayFilterDefinition implements Serializable{
        public GatewayFilterDefinition(String name, LinkedHashMap<String, String> args) {
            this.name = name;
            this.args = args;
        }
        private String name;
        private Map<String,String> args;
    }
}