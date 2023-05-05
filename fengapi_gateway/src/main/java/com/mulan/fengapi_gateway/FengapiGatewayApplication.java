package com.mulan.fengapi_gateway;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class FengapiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(FengapiGatewayApplication.class, args);
    }
}
