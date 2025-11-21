package org.csu.hiscomment.stub;

import org.csu.hiscomment.VO.UserSimpleVO;
import org.csu.hiscomment.feign.UserFeignClient;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * UserFeignClient 的 Stub 实现
 * 用于桩集成测试，模拟用户服务的行为
 * 
 * 注意：这是一个测试用的 Stub 实现，不用于生产环境
 */
@Component
public class UserFeignClientStub implements UserFeignClient {

    // 模拟用户数据存储
    private final Map<Integer, UserSimpleVO> userDatabase = new HashMap<>();
    private final Map<Integer, Boolean> adminDatabase = new HashMap<>();
    private final Map<String, Integer> tokenDatabase = new HashMap<>();

    public UserFeignClientStub() {
        // 初始化测试数据
        initTestData();
    }

    private void initTestData() {
        // 初始化用户数据
        UserSimpleVO user1 = new UserSimpleVO();
        user1.setId(100);
        user1.setUsername("testUser");
        user1.setAvatarUrl("http://example.com/avatar1.jpg");
        userDatabase.put(100, user1);

        UserSimpleVO user2 = new UserSimpleVO();
        user2.setId(200);
        user2.setUsername("adminUser");
        user2.setAvatarUrl("http://example.com/avatar2.jpg");
        userDatabase.put(200, user2);

        // 初始化管理员数据
        adminDatabase.put(200, true);  // user 200 是管理员
        adminDatabase.put(100, false); // user 100 不是管理员

        // 初始化token数据
        tokenDatabase.put("valid-token-100", 100);
        tokenDatabase.put("valid-token-200", 200);
    }

    @Override
    public Map<Integer, UserSimpleVO> getUserSimpleInfoBatch(List<Integer> userIdList) {
        Map<Integer, UserSimpleVO> result = new HashMap<>();
        for (Integer userId : userIdList) {
            UserSimpleVO user = userDatabase.get(userId);
            if (user != null) {
                result.put(userId, user);
            }
        }
        return result;
    }

    @Override
    public boolean isUserExist(int userId) {
        return userDatabase.containsKey(userId);
    }

    @Override
    public boolean isUserAdmin(int userId) {
        return adminDatabase.getOrDefault(userId, false);
    }

    @Override
    public int getUserIdByToken(String token) {
        return tokenDatabase.getOrDefault(token, -1);
    }

    // Stub 特有的辅助方法，用于测试时设置数据
    public void addUser(int userId, String username, String avatarUrl, boolean isAdmin) {
        UserSimpleVO user = new UserSimpleVO();
        user.setId(userId);
        user.setUsername(username);
        user.setAvatarUrl(avatarUrl);
        userDatabase.put(userId, user);
        adminDatabase.put(userId, isAdmin);
    }

    public void removeUser(int userId) {
        userDatabase.remove(userId);
        adminDatabase.remove(userId);
    }

    public void setTokenMapping(String token, int userId) {
        tokenDatabase.put(token, userId);
    }

    public void clearAll() {
        userDatabase.clear();
        adminDatabase.clear();
        tokenDatabase.clear();
        initTestData();
    }
}


