package org.csu.hisuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.mail.internet.MimeMessage;
import org.csu.hisuser.DTO.PasswordResetRequest;
import org.csu.hisuser.DTO.RegisterDTO;
import org.csu.hisuser.entity.User;
import org.csu.hisuser.mapper.UserMapper;
import org.csu.hisuser.service.AuthService;
import org.csu.hisuser.service.UserService;
import org.csu.hisuser.util.JwtUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final JavaMailSender mailSender;
    private final Environment environment;

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;

    public AuthServiceImpl(JwtUtil jwtUtil,
                           RedisTemplate<String,String> redisTemplate,
                           RabbitTemplate rabbitTemplate,
                           JavaMailSender mailSender,
                           Environment environment) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
        this.mailSender = mailSender;
        this.environment = environment;
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
        return register(user,0);
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
    public Integer getUserRoleLevelFromToken(String token) {
        if(!jwtUtil.validateToken(token)) {
            return null;
        }
        // 用户分类ID就是角色等级
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
    public boolean isTeacherTokenValid(String authHeader) {
        if(getUserCategoryIdFromToken(authHeader) < 2) {
            return false;
        }
        return isTokenValid(authHeader);
    }

    @Override
    public boolean isAdminTokenValid(String token) {
        if(getUserCategoryIdFromToken(token) < 3) {
            return false;
        }
        return isTokenValid(token);
    }

    @Override
    public boolean isRootTokenValid(String token) {
        if(getUserCategoryIdFromToken(token) < 4) {
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

    @Override
    public int sendResetPasswordRequest(String username, String email) {
        User user = userService.getUserByUsername(username);
        if(user == null){
            return -1;
        }
        if(!user.getEmail().equals(email)){
            return -2;
        }

        String resetToken = UUID.randomUUID().toString();

        // 存储到Redis (有效期30分钟)
        redisTemplate.opsForValue().set(
                "password:reset:" + resetToken,
                Integer.toString(user.getId()),
                30, TimeUnit.MINUTES
        );

        //发送RabbitMQ
        PasswordResetRequest message = new PasswordResetRequest(
                user.getEmail(),
                resetToken,
                user.getId(),
                user.getUsername(),
                LocalDateTime.now()
        );

        rabbitTemplate.convertAndSend(
                "password.reset.exchange",
                "password.reset.routing.key",
                message
        );

        return 0;
    }

    @Override
    public boolean validateResetToken(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("password:reset:" + token));
    }

    @Override
    public int resetPassword(String token, String newPassword) {
        String userIdString = redisTemplate.opsForValue().get("password:reset:" + token);
        if (userIdString == null) {
            return -1;
        }
        int userId = Integer.parseInt(userIdString);

        // 更新密码
        User user = userService.getUserById(userId);
        if(user == null) {
            return -2;
        }

        user.setPassword(newPassword);
        userMapper.updateById(user);
        // 删除令牌
        redisTemplate.delete("password:reset:" + token);
        return 0;
    }

    @Override
    @RabbitListener(queues = "password.reset.queue")
    public void handlePasswordResetRequest(PasswordResetRequest message) {
        try {
            // 构建邮件内容
            String resetLink = environment.getProperty("app.base-url") +
                    "/auth/forget?token=" + message.getToken();

            String emailContent = buildEmailContent(message.getUsername(), resetLink);

            // 发送邮件
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setTo(message.getEmail());
            helper.setSubject("密码重置请求");
            helper.setText(emailContent, true);
            helper.setFrom(environment.getProperty("spring.mail.username"));

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            // 处理异常，可以记录日志或重试
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
    private String buildEmailContent(String username, String resetLink) {
        return "<html><body>" +
                "<h2>尊敬的 " + username + "：</h2>" +
                "<p>我们收到了您的密码重置请求。请点击以下链接重置您的密码：</p>" +
                "<p><a href=\"" + resetLink + "\">" + resetLink + "</a></p>" +
                "<p>如果您没有请求重置密码，请忽略此邮件。</p>" +
                "<p>此链接将在30分钟后失效。</p>" +
                "</body></html>";
    }
}
