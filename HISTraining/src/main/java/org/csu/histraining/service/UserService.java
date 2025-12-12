package org.csu.histraining.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "his-user-service")
public interface UserService {
    @GetMapping("/inner/user/info/token/{token}/userId")
    public int getUserId(@PathVariable("token") String token);
    @GetMapping("/inner/user/info/token/userId/{userId}/username")
    public String getUsernameById(@PathVariable("userId") int userId);
    @GetMapping("/inner/user/exist/userId/{userId}")
    public boolean isUserIdExist(@PathVariable("userId") int userId);
}
