package org.csu.hisuser.controller;

import org.csu.hisuser.DTO.LoginDTO;
import org.csu.hisuser.DTO.RegisterDTO;
import org.csu.hisuser.entity.User;
import org.csu.hisuser.service.AuthService;
import org.csu.hisuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserAuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

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

        return ResponseEntity.ok(
                Map.of("code",0,
                        "token",token)
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
        if(userCategoryId == -1) {
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","invalid role")
            );
        }
        String token = authService.register(user,userCategoryId);
        if(token == null) {
            return ResponseEntity.internalServerError().body("error to register");
        }
        return ResponseEntity.ok(
                Map.of("code",0,
                        "token",token)
        );
    }
}
