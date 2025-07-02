package org.csu.hiscomment.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
 
@FeignClient(name = "herb-info-service")
public interface HerbFeignClient {
    @GetMapping("/herbs/{herbId}/exist")
    boolean isHerbExist(@PathVariable("herbId") int herbId);
} 