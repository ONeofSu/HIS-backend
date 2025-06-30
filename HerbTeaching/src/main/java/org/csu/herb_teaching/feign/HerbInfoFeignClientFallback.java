package org.csu.herb_teaching.feign;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * HerbInfo Feign 客户端的降级处理类
 * 当 HerbInfo 服务不可用时提供默认响应
 */
@Component
public class HerbInfoFeignClientFallback implements HerbInfoFeignClient {

    @Override
    public Map<String, Object> getHerbInfoById(int herbId) {
        Map<String, Object> fallbackData = new HashMap<>();
        fallbackData.put("code", -1);
        fallbackData.put("message", "HerbInfo 服务暂时不可用");
        
        Map<String, Object> herb = new HashMap<>();
        herb.put("id", herbId);
        herb.put("name", "未知药材");
        herb.put("origin", "未知产地");
        herb.put("image", "default.jpg");
        herb.put("des", "药材信息暂时无法获取");
        
        fallbackData.put("herb", herb);
        return fallbackData;
    }

    @Override
    public Map<String, Object> getHerbInfoByName(String herbName) {
        Map<String, Object> fallbackData = new HashMap<>();
        fallbackData.put("code", -1);
        fallbackData.put("message", "HerbInfo 服务暂时不可用");
        
        Map<String, Object> herb = new HashMap<>();
        herb.put("id", 0);
        herb.put("name", herbName);
        herb.put("origin", "未知产地");
        herb.put("image", "default.jpg");
        herb.put("des", "药材信息暂时无法获取");
        
        fallbackData.put("herb", herb);
        return fallbackData;
    }

    @Override
    public Map<String, Object> getAllHerbs() {
        Map<String, Object> fallbackData = new HashMap<>();
        fallbackData.put("code", -1);
        fallbackData.put("message", "HerbInfo 服务暂时不可用");
        fallbackData.put("herbs", new Object[0]);
        return fallbackData;
    }
} 