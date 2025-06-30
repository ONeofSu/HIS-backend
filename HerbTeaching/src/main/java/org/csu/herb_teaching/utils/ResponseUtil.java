package org.csu.herb_teaching.utils;

import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    
    /**
     * 成功响应
     */
    public static ResponseEntity<Map<String, Object>> success() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 成功响应带数据
     */
    public static ResponseEntity<Map<String, Object>> success(Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.putAll(data);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 成功响应带单个对象
     */
    public static ResponseEntity<Map<String, Object>> success(String key, Object value) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put(key, value);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 错误响应
     */
    public static ResponseEntity<Map<String, Object>> error(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", -1);
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
} 