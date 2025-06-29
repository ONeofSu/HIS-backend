package org.csu.evaluationanddeclaration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("org.csu.evaluationanddeclaration.mapper")
public class EvaluationAndDeclarationApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvaluationAndDeclarationApplication.class, args);
    }
}