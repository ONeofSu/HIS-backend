package org.csu.hisuser.service;

import org.csu.hisuser.entity.User;

public interface AuthService {
    public String login(String username, String password);
    public String register(User user);
    public String register(User user, int userCategoryId);
    public String register(User user, String userCategoryName);
    public String updatePassword(String username, String oldPassword, String newPassword);

    public String getUsernameFromToken(String token);
    public int getUserIdFromToken(String token);    //-1为不存在用户
    public boolean isTokenValid(String token);
}
