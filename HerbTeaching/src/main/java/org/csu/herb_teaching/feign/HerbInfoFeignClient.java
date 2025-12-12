package org.csu.herb_teaching.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * HerbInfo 模块的 Feign 客户端
 * 用于获取药材的基本信息
 */
@FeignClient(name = "herb-info-service", fallback = HerbInfoFeignClientFallback.class)
public interface HerbInfoFeignClient {

    /**
     * 根据药材ID获取药材信息
     */
    @GetMapping("/herbs/info/id/{herbId}")
    Map<String, Object> getHerbInfoById(@PathVariable("herbId") int herbId);

    /**
     * 根据药材名称获取药材信息
     */
    @GetMapping("/herbs/info/name/{herbName}")
    Map<String, Object> getHerbInfoByName(@PathVariable("herbName") String herbName);

    /**
     * 获取所有药材信息
     */
    @GetMapping("/herbs")
    Map<String, Object> getAllHerbs();
} 