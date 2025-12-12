package org.csu.hiscomment.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;
import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 1);
        result.put("message", ex.getMessage());
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> handleFeignException(FeignException ex) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 503);
        result.put("message", "相关服务暂未就绪，请稍后重试");
        return ResponseEntity.status(503).body(result);
    }
} 