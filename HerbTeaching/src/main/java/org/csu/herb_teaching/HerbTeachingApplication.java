package org.csu.herb_teaching;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("org.csu.herb_teaching.mapper")
public class HerbTeachingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HerbTeachingApplication.class, args);
    }

}
