package org.csu.hiscomment.feign;

import org.csu.hiscomment.VO.UserSimpleVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Map;

@FeignClient(name = "his-user-service")
public interface UserFeignClient {
    @PostMapping("/inner/user/info/batch")
    Map<Integer, UserSimpleVO> getUserSimpleInfoBatch(@RequestBody List<Integer> userIdList);

    @GetMapping("/inner/user/exist/userId/{userId}")
    boolean isUserExist(@PathVariable("userId") int userId);

    @GetMapping("/inner/user/is-admin/{userId}")
    boolean isUserAdmin(@PathVariable("userId") int userId);

    @GetMapping("/inner/user/info/token/{token}/userId")
    int getUserIdByToken(@PathVariable("token") String token);
} 