package org.csu.herb_teaching.stub;

import org.csu.herb_teaching.VO.UserVO;

import java.util.HashMap;
import java.util.Map;

/**
 * UserFeignClient 的测试桩（Stub）。
 *
 * 使用内存 Map 维护用户数据，提供：
 * - 用户名 / 头像
 * - 是否为真实教师
 * - 是否存在
 * - token -> userId 映射
 *
 * 在测试中通过注入该 Stub，并配合 Mockito 的 when(...).thenAnswer(...)
 * 来驱动 @MockBean UserFeignClient 的行为。
 */
public class UserFeignClientStub {

    public static class StubUser {
        private final int userId;
        private final String username;
        private final String avatarUrl;
        private final boolean realTeacher;

        public StubUser(int userId, String username, String avatarUrl, boolean realTeacher) {
            this.userId = userId;
            this.username = username;
            this.avatarUrl = avatarUrl;
            this.realTeacher = realTeacher;
        }

        public int getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public boolean isRealTeacher() {
            return realTeacher;
        }
    }

    private final Map<Integer, StubUser> users = new HashMap<>();
    private final Map<String, Integer> tokenToUserId = new HashMap<>();

    public void clearAll() {
        users.clear();
        tokenToUserId.clear();
    }

    public void addUser(int userId, String username, String avatarUrl, boolean realTeacher) {
        users.put(userId, new StubUser(userId, username, avatarUrl, realTeacher));
    }

    public void addToken(String token, int userId) {
        tokenToUserId.put(token, userId);
    }

    public String getUsernameById(int userId) {
        StubUser user = users.get(userId);
        return user != null ? user.getUsername() : null;
    }

    public String getAvatarById(int userId) {
        StubUser user = users.get(userId);
        return user != null ? user.getAvatarUrl() : null;
    }

    public UserVO getUserInfoById(int userId) {
        StubUser user = users.get(userId);
        if (user == null) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setId(user.getUserId());
        vo.setUsername(user.getUsername());
        vo.setAvatarUrl(user.getAvatarUrl());
        return vo;
    }

    public Boolean isUserRealTeacher(int userId) {
        StubUser user = users.get(userId);
        return user != null && user.isRealTeacher();
    }

    public Boolean isUserExist(int userId) {
        return users.containsKey(userId);
    }

    public Integer getUserIdByToken(String token) {
        return tokenToUserId.getOrDefault(token, 0);
    }
}


