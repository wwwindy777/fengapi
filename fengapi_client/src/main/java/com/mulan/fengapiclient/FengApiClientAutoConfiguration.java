package com.mulan.fengapiclient;

import com.mulan.fengapiclient.client.FengApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 客户端自动配置类
 */
@Configuration
@ConfigurationProperties(prefix = "fengapi.client")
@Data
public class FengApiClientAutoConfiguration {
    private String accessKey;
    private String secretKey;
    @Bean
    public FengApiClient fengApiClient(){
        return new FengApiClient(accessKey,secretKey);
    }
}
