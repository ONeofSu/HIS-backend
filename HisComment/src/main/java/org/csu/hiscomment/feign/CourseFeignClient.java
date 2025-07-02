package org.csu.hiscomment.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "his-gateway")
public interface CourseFeignClient {
    @GetMapping("/herb-teaching-service/courses/{courseId}/exist")
    boolean isCourseExist(@PathVariable("courseId") int courseId);
} 