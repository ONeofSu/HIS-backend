package org.csu.hisuser.controller;

import jakarta.ws.rs.PUT;
import org.csu.hisuser.DTO.ResetPswDTO;
import org.csu.hisuser.DTO.UpdateUserDTO;
import org.csu.hisuser.VO.UserVO;
import org.csu.hisuser.entity.User;
import org.csu.hisuser.service.AuthService;
import org.csu.hisuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @GetMapping("/account/info/me")
    public ResponseEntity<?> getUserPersonalInfo(@RequestHeader("Authorization") String authHeader){
        if(!authService.isAuthHeaderValid(authHeader)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token or user does not exist")
            );
        }

        String token = authHeader.substring(7);
        User user = userService.getUserByUsername(authService.getUsernameFromToken(token));
        UserVO userVO = userService.transferUserToUserVO(user);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "user",userVO)
        );
    }

    @PutMapping("/account/info/me")
    public ResponseEntity<?> updateUserPersonalInfo(@RequestHeader("Authorization") String authHeader,
                                                    @RequestBody UpdateUserDTO updateUserDTO){
        String token = authHeader.substring(7);
        if(!authService.isTokenValid(token)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token or user does not exist")
            );
        }

        if(!userService.isUserExist(updateUserDTO.getUserId())){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","the user does not exist")
            );
        }

        if(!authService.getUsernameFromToken(token).
                equals(userService.getUserById(updateUserDTO.getUserId()).getUsername())){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you can't update other user")
            );
        }

        User user = userService.transferUpdateUserToUserVO(updateUserDTO);
        if(!userService.updateUserInfo(user)){
            return ResponseEntity.internalServerError().body("error to update user");
        }
        UserVO userVO = userService.transferUserToUserVO(user);
        return ResponseEntity.ok(
                Map.of("code",0
                ,"user",userVO)
        );
    }

    @PostMapping("/account/password/reset")
    public ResponseEntity<?> resetPassword(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody ResetPswDTO resetPswDTO){
        String token = authHeader.substring(7);
        if(!authService.isTokenValid(token)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token or user does not exist")
            );
        }

        String resetResult = authService.updatePassword(resetPswDTO.getUsername(),
                resetPswDTO.getOldPassword(), resetPswDTO.getNewPassword());
        if(resetResult == null){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","invalid username or old password")
            );
        }

        if(!authService.getUsernameFromToken(token).
                equals(resetPswDTO.getUsername())){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you can't update other user")
            );
        }

        return ResponseEntity.ok(
                Map.of("code",0,
                        "token",resetResult)
        );
    }

    @DeleteMapping("/account/{userId}")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String authHeader,
                                        @PathVariable("userId") int userId){
        String token = authHeader.substring(7);
        if(!authService.isTokenValid(token)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token or user does not exist")
            );
        }

        User user = userService.getUserById(userId);
        if(user == null){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","the user you want to delete does not exist")
            );
        }
        UserVO userVO = userService.transferUserToUserVO(user);

        if(userService.isUserLinkCategoryExist(userId,4)){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","root admin can't be deleted")
            );
        }

        if(!userService.deleteUser(userId)){
            return ResponseEntity.internalServerError().body("error to delete user");
        }
        return ResponseEntity.ok(
                Map.of("code",0,
                        "user",userVO)
        );
    }

    @PostMapping("/account/exit")
    public ResponseEntity<?> logOut(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        if(!authService.isTokenValid(token)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        authService.exitLogin(token);
        return ResponseEntity.ok(
                Map.of("code",0)
        );
    }
}
