package org.csu.herbinfo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Value("${ai.service.url:http://192.168.51.11:8000}")
    private String aiServiceUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AIServiceConfig aiServiceConfig() {
        return new AIServiceConfig(aiServiceUrl);
    }
}

