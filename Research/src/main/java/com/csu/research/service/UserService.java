package com.csu.research.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "his-user-service")
public interface UserService {
    @GetMapping("/inner/user/info/token/{token}/userId")
    int getUserId(@PathVariable("token") String token);
    @GetMapping("/inner/user/info/token/userId/{userId}/username")
    String getUsernameById(@PathVariable("userId") int userId);
    @GetMapping("/inner/user/exist/userId/{userId}")
    boolean isUserIdExist(@PathVariable("userId") int userId);
    @GetMapping("/inner/user/is-admin/{userId}")
    boolean isAdmin(@PathVariable("userId") int userId);
}
