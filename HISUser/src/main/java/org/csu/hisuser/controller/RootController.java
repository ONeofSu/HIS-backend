package org.csu.hisuser.controller;

import org.csu.hisuser.DTO.AddInviteDTO;
import org.csu.hisuser.DTO.RegisterDTO;
import org.csu.hisuser.DTO.UpdateUserDTO;
import org.csu.hisuser.VO.UserVO;
import org.csu.hisuser.entity.InvitationCode;
import org.csu.hisuser.entity.User;
import org.csu.hisuser.service.AuthService;
import org.csu.hisuser.service.InviteService;
import org.csu.hisuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/root")
public class RootController {
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;
    @Autowired
    InviteService inviteService;

    @GetMapping("/user/info/all")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if(!authService.isAdminTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        List<User> users = userService.getAllUsers();
        List<UserVO> userVOS = userService.transferUsersToUserVOs(users);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "users",userVOS)
        );
    }

    @GetMapping("/user/info/{userId}")
    public ResponseEntity<?> getUserById(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable int userId) {
        String token = authHeader.substring(7);
        if(!authService.isAdminTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!userService.isUserExist(userId)) {
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","the user does not exist")
            );
        }

        User user = userService.getUserById(userId);
        UserVO userVO = userService.transferUserToUserVO(user);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "user",userVO)
        );
    }

    @PutMapping("/user/info")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String authHeader,
                                        @RequestBody UpdateUserDTO updateUserDTO) {
        String token = authHeader.substring(7);
        if(!authService.isAdminTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!userService.isUserExist(updateUserDTO.getUserId())) {
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","the user does not exist")
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

    @GetMapping("/user/category/{categoryName}")
    public ResponseEntity<?> getUserByCategory(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable String categoryName) {
        String token = authHeader.substring(7);
        if(!authService.isAdminTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!userService.isUserCategoryExistByName(categoryName)){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","invalid categoryName")
            );
        }

        int categoryId = userService.getUserCategoryIdByCategoryName(categoryName);
        List<User> user = userService.getAllUserOfCategory(categoryId);
        List<UserVO> userVOS = userService.transferUsersToUserVOs(user);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "users",userVOS)
        );
    }

    @PostMapping("/admin")
    public ResponseEntity<?> addAdmin(@RequestHeader("Authorization") String authHeader,
                                      @RequestBody RegisterDTO registerDTO) {
        String token = authHeader.substring(7);
        if(!authService.isRootTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","only root admin can do this")
            );
        }

        if(userService.isUsernameExist(registerDTO.getUsername())) {
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","user already exist")
            );
        }

        registerDTO.setRole("管理员");
        User user = authService.transferRegisterDTOToUser(registerDTO);
        authService.register(user,3);

        UserVO userVO = userService.transferUserToUserVO(user);
        userVO.setRole("管理员");
        return ResponseEntity.ok(
                Map.of("code",0,
                        "user",userVO)

        );
    }

    @PostMapping("/teacher/invite")
    public ResponseEntity<?> generateTeacherCode(@RequestHeader("Authorization") String authHeader,
                                                 @RequestBody AddInviteDTO addInviteDTO) {
        String token = authHeader.substring(7);
        if(!authService.isAdminTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }
        int creatorUserId = authService.getUserIdFromToken(token);

        InvitationCode invitationCode = inviteService.generateTeacherInviteCode(creatorUserId
                ,addInviteDTO.getSchoolName(),addInviteDTO.getUserName());

        return ResponseEntity.ok(
                Map.of("code",0,
                        "invitationCode",invitationCode)
        );
    }

    @DeleteMapping("/teacher/invite/{invitationCodeId}")
    public ResponseEntity<?> deleteTeacherCode(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable Long invitationCodeId) {
        String token = authHeader.substring(7);
        if(!authService.isAdminTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }
         if(!inviteService.isInviteCodeExist(invitationCodeId)) {
             return ResponseEntity.ok(
                     Map.of("code",-2,
                             "message","the code does not exist")
             );
         }

         InvitationCode invitationCode = inviteService.getInviteCode(invitationCodeId);
         if(invitationCode.getCodeIsUsed()){
             return ResponseEntity.ok(
                     Map.of("code",-3,
                             "message","the code is used and you can't delete it")
             );
         }

         inviteService.deleteInviteCode(invitationCodeId);
         return ResponseEntity.ok(
                 Map.of("code",0,
                         "codeId",invitationCodeId)
         );
    }

    @GetMapping("/codes")
    public ResponseEntity<?> getCodes(@RequestHeader("Authorization") String authHeader,
                                      @RequestParam(defaultValue = "1")int page,
                                      @RequestParam(defaultValue = "10")int size) {
        String token = authHeader.substring(7);
        if(!authService.isAdminTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        List<InvitationCode> codes = inviteService.getAllInviteCodes(page, size);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "invitationCodes",codes)
        );
    }

    @GetMapping("/codes/notUsed")
    public ResponseEntity<?> getCodesNotUsed(@RequestHeader("Authorization") String authHeader,
                                             @RequestParam(defaultValue = "1")int page,
                                             @RequestParam(defaultValue = "10")int size) {
        String token = authHeader.substring(7);
        if(!authService.isAdminTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        List<InvitationCode> codes = inviteService.getAllInviteCodesThatNotUsed(page, size);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "invitationCodes",codes)
        );
    }

    @GetMapping("/codes/used")
    public ResponseEntity<?> getCodesUsed(@RequestHeader("Authorization") String authHeader,
                                          @RequestParam(defaultValue = "1")int page,
                                          @RequestParam(defaultValue = "10")int size) {
        String token = authHeader.substring(7);
        if(!authService.isAdminTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        List<InvitationCode> codes = inviteService.getAllInviteCodesThatUsed(page, size);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "invitationCodes",codes)
        );
    }
}
