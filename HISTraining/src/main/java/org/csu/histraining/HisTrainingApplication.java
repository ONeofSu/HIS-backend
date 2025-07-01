package org.csu.histraining;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@MapperScan("org.csu.histraining.mapper")
public class HisTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HisTrainingApplication.class, args);
    }

}
