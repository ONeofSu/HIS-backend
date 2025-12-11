package org.csu.performance.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 用户服务Feign客户端
 */
@FeignClient(name = "his-user-service")
public interface UserFeignClient {
    
    /**
     * 根据token获取用户ID
     * @param token JWT token
     * @return 用户ID
     */
    @GetMapping("/inner/user/info/token/{token}/userId")
    int getUserIdByToken(@PathVariable("token") String token);
    
    /**
     * 根据token获取用户角色等级
     * @param token JWT token
     * @return 用户角色等级 (1:学生, 2:教师, 3:管理员)
     */
    @GetMapping("/inner/user/info/token/{token}/roleLevel")
    Integer getUserRoleLevelByToken(@PathVariable("token") String token);
    
    /**
     * 验证用户是否存在
     * @param userId 用户ID
     * @return 是否存在
     */
    @GetMapping("/inner/user/exist/userId/{userId}")
    Boolean isUserExist(@PathVariable("userId") int userId);
    
    /**
     * 判断是否为教师
     * @param userId 用户ID
     * @return 是否为教师
     */
    @GetMapping("/inner/user/is-real-teacher/{userId}")
    Boolean isUserRealTeacher(@PathVariable("userId") int userId);
    
    /**
     * 判断是否为管理员
     * @param userId 用户ID
     * @return 是否为管理员
     */
    @GetMapping("/inner/user/is-admin/{userId}")
    Boolean isUserAdmin(@PathVariable("userId") int userId);
    
    /**
     * 根据用户ID获取用户详细信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/inner/user/info/id/{userId}")
    Map<String, Object> getUserInfoById(@PathVariable("userId") int userId);
    
    /**
     * 批量获取用户信息
     * @param userIdList 用户ID列表
     * @return 用户信息Map
     */
    @PostMapping("/inner/user/info/batch")
    Map<Integer, Map<String, Object>> getUserInfoBatch(@RequestBody List<Integer> userIdList);
} 