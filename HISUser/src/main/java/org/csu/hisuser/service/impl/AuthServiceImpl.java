package org.csu.hisuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.hisuser.DTO.RegisterDTO;
import org.csu.hisuser.entity.User;
import org.csu.hisuser.mapper.UserMapper;
import org.csu.hisuser.service.AuthService;
import org.csu.hisuser.service.UserService;
import org.csu.hisuser.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;

    public AuthServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("password", password);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return null;
        }
        return jwtUtil.generateToken(username, userService.getCategoryOnUser(userService.getIdByUsername(username)).getId());
    }

    @Override
    public String register(User user) {
        return register(user,1);
    }

    @Override
    public String register(User user, int userCategoryId) {
        if(userService.isUsernameExist(user.getUsername())) {
            return null;
        }
        if(!userService.isUserCategoryExistById(userCategoryId)) {
            return null;
        }
        userMapper.insert(user);
        userService.addUserLinkCategory(userService.getIdByUsername(user.getUsername()),userCategoryId);
        return jwtUtil.generateToken(user.getUsername(),userCategoryId);
    }

    @Override
    public String register(User user, String userCategoryName) {
        int userCategoryId = userService.getUserCategoryByCategoryName(userCategoryName).getId();
        return register(user,userCategoryId);
    }

    @Override
    public String updatePassword(String username,String oldPassword, String newPassword) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("password", oldPassword);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null) {
            return null;
        }
        user.setPassword(newPassword);
        userMapper.updateById(user);
        return jwtUtil.generateToken(username, userService.getCategoryOnUser(userService.getIdByUsername(username)).getId());
    }

    @Override
    public String getUsernameFromToken(String token) {
        if(!jwtUtil.validateToken(token)) {
            return null;
        }
        return jwtUtil.extractUsername(token);
    }

    @Override
    public int getUserIdFromToken(String token) {
        String username = getUsernameFromToken(token);
        if(username == null) {
            return -1;
        }
        return userService.getIdByUsername(username);
    }

    @Override
    public int getUserCategoryIdFromToken(String token) {
        if(!jwtUtil.validateToken(token)) {
            return -1;
        }
        return jwtUtil.extractRoleLevel(token);
    }

    @Override
    public boolean isTokenValid(String token) {
        if(!jwtUtil.validateToken(token)) {
            return false;
        }

        String username = getUsernameFromToken(token);
        if(username == null) {
            return false;
        }
        return userService.isUsernameExist(username);
    }

    @Override
    public boolean isAuthHeaderValid(String authHeader) {
        if(authHeader == null) {
            return false;
        }
        String token = authHeader.substring(7);
        return isTokenValid(token);
    }

    @Override
    public boolean isRootTokenValid(String token) {
        if(getUserCategoryIdFromToken(token) != 3) {
            return false;
        }
        return isTokenValid(token);
    }

    @Override
    public User transferRegisterDTOToUser(RegisterDTO registerDTO) {
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        if(registerDTO.getAvatarUrl()!=null){
            user.setAvatarUrl(registerDTO.getAvatarUrl());
        }
        return user;
    }
}
