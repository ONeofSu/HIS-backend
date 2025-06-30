package org.csu.herb_teaching.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test-resources")
public class TestResourceController {

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Test Resource Controller is working!");
        result.put("code", 0);
        return result;
    }

    @GetMapping("/labs/{labId}/resources")
    public Map<String, Object> testGetLabResources(@PathVariable int labId) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Test get lab resources for labId: " + labId);
        result.put("labId", labId);
        result.put("code", 0);
        return result;
    }
} 