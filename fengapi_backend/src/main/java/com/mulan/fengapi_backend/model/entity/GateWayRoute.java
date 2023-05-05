package com.mulan.fengapi_backend.model.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * GateWay路由（没有filter）
 * @author wwwwind
 */
@Data
public class GateWayRoute {
    private String id;
    private ArrayList<Predicate> predicates;
    private String url;
    private Integer order;
    @Data
    public static class Predicate {
        public Predicate(String name, LinkedHashMap<String, String> args) {
            this.name = name;
            this.args = args;
        }
        private String name;
        private LinkedHashMap<String,String> args;
    }
}
