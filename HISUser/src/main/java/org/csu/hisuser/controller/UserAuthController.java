package org.csu.hisuser.controller;

import org.csu.hisuser.DTO.LoginDTO;
import org.csu.hisuser.DTO.PasswordResetDTO;
import org.csu.hisuser.DTO.RegisterDTO;
import org.csu.hisuser.DTO.SchoolRegisterDTO;
import org.csu.hisuser.VO.UserVO;
import org.csu.hisuser.entity.User;
import org.csu.hisuser.service.AuthService;
import org.csu.hisuser.service.InviteService;
import org.csu.hisuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserAuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private InviteService inviteService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        if(!userService.isUsernameExist(loginDTO.getUsername())) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","user does not exist")
            );
        }

        String token = authService.login(loginDTO.getUsername(), loginDTO.getPassword());
        if(token == null) {
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","invalid username or password")
            );
        }

        UserVO userVO = userService.transferUserToUserVO(userService.getUserByUsername(loginDTO.getUsername()));

        return ResponseEntity.ok(
                Map.of("code",0,
                        "token",token,
                        "user",userVO)
        );
    }

    @PostMapping("/register/verify/email/{email}")
    public ResponseEntity<?> verifyEmail(@PathVariable("email") String email) {
        if(authService.isEmailUsed(email)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","email has been used")
            );
        }

        authService.generateVerificationCode(email);
        authService.sendVerificationEmail(email);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "email",email)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        if(userService.isUsernameExist(registerDTO.getUsername())) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","user already exist")
            );
        }

        User user = authService.transferRegisterDTOToUser(registerDTO);
        int userCategoryId = userService.getUserCategoryIdByCategoryName(registerDTO.getRole());

        if(userCategoryId != 0) {
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","invalid role")
            );
        }

        if(registerDTO.getEmailVerifyCode() == null ||
                !authService.validateVerificationCode(registerDTO.getEmail(),registerDTO.getEmailVerifyCode())){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","invalid email code")
            );
        }

        String token = authService.register(user,userCategoryId);
        if(token == null) {
            return ResponseEntity.internalServerError().body("error to register");
        }

        UserVO userVO = userService.transferUserToUserVO(user);

        return ResponseEntity.ok(
                Map.of("code",0,
                        "token",token,
                        "user",userVO)
        );
    }

    @PostMapping("/register/school")
    @Transactional
    public ResponseEntity<?> registerSchoolUsers(@RequestBody SchoolRegisterDTO schoolRegisterDTO){
        if(userService.isUsernameExist(schoolRegisterDTO.getUsername())) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","user already exist")
            );
        }

        if(schoolRegisterDTO.getEmailVerifyCode() == null ||
                !authService.validateVerificationCode(schoolRegisterDTO.getEmail(),schoolRegisterDTO.getEmailVerifyCode())){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","invalid email code")
            );
        }


        int userCategoryId = userService.getUserCategoryIdByCategoryName(schoolRegisterDTO.getRole());
        //0表示成功 -1邀请码不正确 -2注册类型和邀请码类型不符 -3学校名不正确 -4姓名不正确 -5邀请码已被使用 -6邀请码过期
        int flag = inviteService.useInviteCode(schoolRegisterDTO.getInvitationCode(),userCategoryId
                ,schoolRegisterDTO.getSchoolName(), schoolRegisterDTO.getUserName());
        if(flag == -1) {
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","invalid invitation code")
            );
        }
        if(flag == -2) {
            return ResponseEntity.ok(
                    Map.of("code",-4,
                            "message","incorrect category id")
            );
        }
        if(flag == -3) {
            return ResponseEntity.ok(
                    Map.of("code",-5,
                            "message","incorrect school name")
            );
        }
        if(flag == -4) {
            return ResponseEntity.ok(
                    Map.of("code",-6,
                            "message","incorrect user name")
            );
        }
        if(flag == -5) {
            return ResponseEntity.ok(
                    Map.of("code",-7,
                            "message","code has been used")
            );
        }
        if(flag == -6) {
            return ResponseEntity.ok(
                    Map.of("code",-8,
                            "message","code is expired")
            );
        }

        User user = authService.transferRegisterDTOToUser(schoolRegisterDTO);
        String token = authService.register(user,userCategoryId);
        user = userService.getUserByUsername(schoolRegisterDTO.getUsername());
        inviteService.addLinkInvitation(user.getId(), inviteService.getInvitationCodeIdByCode(schoolRegisterDTO.getInvitationCode()));
        UserVO userVO = userService.transferUserToUserVO(user);

        return ResponseEntity.ok(
                Map.of("code",0,
                        "token",token,
                        "user",userVO)
        );
    }

    @PostMapping("/forget/send")
    public ResponseEntity<?> forget(@RequestBody PasswordResetDTO passwordResetDTO) {
        if(!userService.isUsernameExist(passwordResetDTO.getUsername())) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","user does not exist")
            );
        }

        int flag = authService.sendResetPasswordRequest(passwordResetDTO.getUsername(), passwordResetDTO.getEmail());
        if(flag == -2) {
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","incorrect email")
            );
        }

        return ResponseEntity.ok(
                Map.of("code",0,
                        "username", passwordResetDTO.getUsername(),
                        "email", passwordResetDTO.getEmail())
        );
    }

    @GetMapping("/forget/valid")
    public ResponseEntity<?> forgetTokenCheck(@RequestParam("token") String token) {
        boolean isValid = authService.validateResetToken(token);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "result",isValid)
        );
    }

    @PostMapping("/forget/reset/{newPassword}")
    public ResponseEntity<?> resetPasswordForForget(@RequestParam String token,
                                                    @PathVariable String newPassword) {
        int flag = authService.resetPassword(token, newPassword);
        if(flag == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }
        if(flag == -2) {
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","invalid user")
            );
        }

        return ResponseEntity.ok(
                Map.of("code",0,
                        "new password",newPassword)
        );
    }
}
