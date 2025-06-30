package org.csu.hisuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.hisuser.DTO.UpdateUserDTO;
import org.csu.hisuser.VO.UserVO;
import org.csu.hisuser.entity.User;
import org.csu.hisuser.entity.UserCategory;
import org.csu.hisuser.entity.UserLinkCategory;
import org.csu.hisuser.mapper.UserCategoryMapper;
import org.csu.hisuser.mapper.UserLinkCategoryMapper;
import org.csu.hisuser.mapper.UserMapper;
import org.csu.hisuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserCategoryMapper userCategoryMapper;
    @Autowired
    UserLinkCategoryMapper userLinkCategoryMapper;

    //---------------------------------------------------User-----------------------------------------------------
//    @Override
//    public boolean login(String username, String password) {
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username", username).eq("password", password);
//        User user = userMapper.selectOne(queryWrapper);
//        if (user == null) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean register(User user) {
//        if(isUsernameExist(user.getUsername())) {
//            return false;
//        }
//        userMapper.insert(user);
//        return true;
//    }

    @Override
    public boolean isUsernameExist(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean isUserExist(int id) {
        User user = userMapper.selectById(id);
        if(user == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateUserInfo(User user) {
        if(!isUserExist(user.getId())) {
            return false;
        }
        if(!isUsernameExist(user.getUsername())) {
            return false;
        }
        User newUser = new User();
        newUser.setId(user.getId());
        //newUser.setUsername(user.getUsername());
        //newUser.setPassword(user.getPassword()); 不设置密码
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        if(user.getAvatarUrl() != null) {
            newUser.setAvatarUrl(user.getAvatarUrl());
        }else{
            newUser.setAvatarUrl(null);
        }

        userMapper.updateById(user);
        return true;
    }

    @Override
    public boolean deleteUser(int id) {
        if(!isUserExist(id)) {
            return false;
        }
        deleteLinkOnUser(id);
        userMapper.deleteById(id);
        return true;
    }

    @Override
    public int getIdByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    @Override
    public User getUserById(int id) {
        return userMapper.selectById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        int id = getIdByUsername(username);
        if(id == -1) {
            return null;
        }
        return getUserById(id);
    }

    //----------------------------------------------UserCategory------------------------------------------------
    @Override
    public List<UserCategory> getAllUserCategory() {
        return userCategoryMapper.selectList(null);
    }

    @Override
    public boolean isUserCategoryExistByName(String categoryName) {
        UserCategory userCategory = new UserCategory();
        userCategory = getUserCategoryByCategoryName(categoryName);
        if(userCategory == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isUserCategoryExistById(int id) {
        UserCategory userCategory = getUserCategoryById(id);
        if(userCategory == null) {
            return false;
        }
        return true;
    }

    @Override
    public UserCategory getUserCategoryByCategoryName(String categoryName) {
        QueryWrapper<UserCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_name", categoryName);
        return userCategoryMapper.selectOne(queryWrapper);
    }

    @Override
    public int getUserCategoryIdByCategoryName(String categoryName) {
        QueryWrapper<UserCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_name", categoryName);
        UserCategory userCategory = userCategoryMapper.selectOne(queryWrapper);
        if(userCategory == null) {
            return -1;
        }
        return userCategory.getId();
    }

    @Override
    public UserCategory getUserCategoryById(int id) {
        return userCategoryMapper.selectById(id);
    }

    //-------------------------------------------UserLinkCategory---------------------------------------------
    @Override
    public boolean isUserLinkCategoryExist(int userId, int categoryId) {
        QueryWrapper<UserLinkCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("category_id", categoryId);
        if(userLinkCategoryMapper.selectCount(queryWrapper) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean addUserLinkCategory(int userId,int categoryId) {
        if(!isUserExist(userId) || !isUserCategoryExistById(categoryId)) {
            return false;
        }
        if(isUserLinkCategoryExist(categoryId, userId)) {
            return false;
        }
        UserLinkCategory userLinkCategory = new UserLinkCategory();
        userLinkCategory.setUserId(userId);
        userLinkCategory.setCategoryId(categoryId);
        userLinkCategoryMapper.insert(userLinkCategory);
        return true;
    }

    @Override
    public boolean deleteLinkOnUser(int userId) {
        if(!isUserExist(userId)) {
            return false;
        }
        QueryWrapper<UserLinkCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        userLinkCategoryMapper.delete(queryWrapper);
        return true;
    }

    @Override
    public List<User> getAllUserOfCategory(int categoryId) {
        if(!isUserCategoryExistById(categoryId)) {
            return null;
        }
        QueryWrapper<UserLinkCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        List<UserLinkCategory> userLinkCategories = userLinkCategoryMapper.selectList(queryWrapper);
        List<User> users = new ArrayList<>();
        for(UserLinkCategory userLinkCategory : userLinkCategories) {
            User user = getUserById(userLinkCategory.getUserId());
            users.add(user);
        }
        return users;
    }

    @Override
    public UserCategory getCategoryOnUser(int userId) {
        if(!isUserExist(userId)) {
            return null;
        }
        QueryWrapper<UserLinkCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        UserLinkCategory userLinkCategory = userLinkCategoryMapper.selectOne(queryWrapper);
        if(userLinkCategory == null) {
            return null;
        }
        return getUserCategoryById(userLinkCategory.getCategoryId());
    }

    //---------------------------------------------------ELSE--------------------------------------------------
    @Override
    public UserVO transferUserToUserVO(User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setEmail(user.getEmail());
        userVO.setPhone(user.getPhone());
        if(user.getAvatarUrl() != null) {
            userVO.setAvatarUrl(user.getAvatarUrl());
        }

        UserCategory category = getCategoryOnUser(user.getId());
        if(category != null) {
            userVO.setRole(category.getName());
        }else{
            userVO.setRole("null");
        }

        return userVO;
    }

    @Override
    public List<UserVO> transferUsersToUserVOs(List<User> users) {
        List<UserVO> userVOs = new ArrayList<>();
        for(User user : users) {
            UserVO userVO = transferUserToUserVO(user);
            userVOs.add(userVO);
        }
        return userVOs;
    }

    @Override
    public User transferUpdateUserToUserVO(UpdateUserDTO updateUserDTO) {
        User user = getUserById(updateUserDTO.getUserId());
        user.setEmail(updateUserDTO.getEmail());
        user.setPhone(updateUserDTO.getPhone());
        if(updateUserDTO.getAvatarUrl() != null) {
            user.setAvatarUrl(updateUserDTO.getAvatarUrl());
        }
        return user;
    }


}
