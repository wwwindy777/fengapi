package com.mulan.fengapi_backend;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableDubbo
@MapperScan("com/mulan/fengapi_backend/mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
public class FengapiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FengapiBackendApplication.class, args);
    }

}
