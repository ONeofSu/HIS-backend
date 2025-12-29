package org.csu.histraining.sub;

import org.csu.histraining.service.UserService;
import java.util.*;

/**
 * UserService 的桩实现
 * 模拟用户服务，提供预定义的用户数据
 */
public class UserServiceStub implements UserService {

    // 模拟用户数据
    private final Map<Integer, String> userMap = new HashMap<Integer, String>() {{
        put(1, "测试用户A");
        put(2, "测试用户B");
        put(3, "测试用户C");
    }};

    @Override
    public boolean isUserIdExist(int userId) {
        return userMap.containsKey(userId);
    }

    @Override
    public String getUsernameById(int userId) {
        return userMap.getOrDefault(userId, "未知用户");
    }

    @Override
    public int getUserId(String token) {
        // 模拟 token 解析，返回固定用户 ID
        if ("valid_token".equals(token)) {
            return 1;
        }
        return -1;
    }

    // 其他方法省略或抛出异常
}
