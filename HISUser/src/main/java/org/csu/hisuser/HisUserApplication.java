package org.csu.hisuser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.csu.hisuser.mapper")
public class HisUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(HisUserApplication.class, args);
    }

}
