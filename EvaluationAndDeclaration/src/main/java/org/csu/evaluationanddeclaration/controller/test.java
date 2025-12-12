package org.csu.evaluationanddeclaration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {

    @GetMapping("/hello")
    public String foo(
            @RequestBody NameAgeRequest nameAgeRequest
    ) {
        return nameAgeRequest.getName();
    }

    @PostMapping("/echo")
    public ResponseEntity<?> echo(
            @RequestBody NameAgeRequest nameAgeRequest
    ) {
        System.out.println("getName: " + nameAgeRequest.getName());
        System.out.println("getAge: " + nameAgeRequest.getAge());
        nameAgeRequest.setCode(-1);
        nameAgeRequest.setName("Jack");
        return ResponseEntity.ok(nameAgeRequest);
    }
}


class NameAgeRequest {
    private String name;
    private int age;
    private int code;

    public void setCode(int code) {
        this.code = code;
    }

    // 必须有无参构造函数
    public NameAgeRequest() {}

    public NameAgeRequest(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}