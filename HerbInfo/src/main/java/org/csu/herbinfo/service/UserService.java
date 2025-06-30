package org.csu.herbinfo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value="his-user-service")
public interface UserService {
    @GetMapping("/inner/user/info/token/{token}/userId")
    public int getUserId(@PathVariable("token") String token);
}
