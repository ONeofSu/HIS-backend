package org.csu.herb_teaching.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.csu.herb_teaching.VO.UserVO;

@FeignClient(name = "his-user-service")
public interface UserFeignClient {
    @GetMapping("/inner/user/info/id/{userId}/username")
    String getUsernameById(@PathVariable("userId") int userId);

    @GetMapping("/inner/user/info/id/{userId}/avatar")
    String getAvatarById(@PathVariable("userId") int userId);

    @GetMapping("/inner/user/info/id/{userId}")
    UserVO getUserInfoById(@PathVariable("userId") int userId);

    @GetMapping("/inner/user/is-real-teacher/{userId}")
    Boolean isUserRealTeacher(@PathVariable("userId") int userId);

    @GetMapping("/inner/user/exist/userId/{userId}")
    Boolean isUserExist(@PathVariable("userId") int userId);

    @GetMapping("/inner/user/info/token/{token}/userId")
    int getUserIdByToken(@PathVariable("token") String token);
} 