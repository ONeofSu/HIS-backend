package org.csu.herbinfo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value="his-user-service")
public interface UserService {
    @GetMapping("/inner/user/info/token/{token}/userId")
    public int getUserId(@PathVariable("token") String token);
    @GetMapping("/inner/user/exist/userId/{userId}")
    public boolean isUserIdExist(@PathVariable("userId") int userId);
    @GetMapping("/inner/user/info/token/{token}/userId")
    int getUserIdByToken(@PathVariable("token") String token);
    @GetMapping("/inner/user/is-admin/{userId}")
    boolean isAdmin(@PathVariable("userId") int userId);
    @GetMapping("/inner/token/admin/valid/{token}")
    boolean isAdminToken(@PathVariable("token") String token);
}
