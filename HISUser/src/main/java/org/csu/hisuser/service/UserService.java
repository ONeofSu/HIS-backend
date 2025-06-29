package org.csu.hisuser.service;

import org.csu.hisuser.entity.User;
import org.csu.hisuser.entity.UserCategory;
import org.csu.hisuser.entity.UserLinkCategory;

import java.util.List;

public interface UserService {
    //public boolean login(String username, String password);
    //public boolean register(User user);
    public boolean isUsernameExist(String username);
    public boolean isUserExist(int id);
    public boolean updateUserInfo(User user);   //不修改密码
    public boolean deleteUser(int id);
    public int getIdByUsername(String username);    //不存在返回-1
    public List<User> getAllUsers();
    public User getUserById(int id);
    public User getUserByUsername(String username);

    public List<UserCategory> getAllUserCategory();
    public UserCategory getUserCategoryByCategoryName(String categoryName);
    public UserCategory getUserCategoryById(int id);
    public boolean isUserCategoryExistByName(String categoryName);
    public boolean isUserCategoryExistById(int id);

    public boolean isUserLinkCategoryExist(int userId,int categoryId);
    public boolean addUserLinkCategory(int userId,int categoryId);
    public boolean deleteLinkOnUser(int userId);
    public List<User> getAllUserOfCategory(int categoryId);
    public UserCategory getCategoryOnUser(int userId);
}
