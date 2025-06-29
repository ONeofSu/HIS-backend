package com.csu.research.controller;

import com.csu.research.entity.Auth;
import com.csu.research.mapper.AuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {
    @Autowired
    private AuthMapper authMapper;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        Auth auth = authMapper.selectById(1);
        return ResponseEntity.ok(
                Map.of("code", 0,
                        "data", auth)
        );
    }
}
