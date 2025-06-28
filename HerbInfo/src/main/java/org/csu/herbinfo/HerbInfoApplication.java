package org.csu.herbinfo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.csu.herbinfo.mapper")
public class HerbInfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HerbInfoApplication.class, args);
    }

}
