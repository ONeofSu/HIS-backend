package org.csu.hisuser.controller;

import org.csu.hisuser.DTO.AddInviteDTO;
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
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    InviteService inviteService;
    @Autowired
    AuthService authService;
    @Autowired
    UserService userService;

    @PostMapping("/invite/student")
    public ResponseEntity<?> generateStudentCode(@RequestHeader("Authorization") String authHeader,
                                                 @RequestBody AddInviteDTO addInviteDTO){
        String token = authHeader.substring(7);
        if(!authService.isTeacherTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }
        int creatorUserId = authService.getUserIdFromToken(token);

        //教师创建的邀请码应与教师学校一致
        if(userService.getCategoryOnUser(creatorUserId).getId() == 2) {
            String userSchool = inviteService.getSchoolNameByUserId(creatorUserId);
            if(userSchool == null || userSchool.isEmpty() || !userSchool.equals(addInviteDTO.getSchoolName())) {
                return ResponseEntity.ok(
                        Map.of("code",-2,
                                "message","the school name should be same as the teacher's school")
                );
            }
        }

        InvitationCode invitationCode = inviteService.generateStudentInviteCode(creatorUserId,
                addInviteDTO.getSchoolName(),addInviteDTO.getUserName());
        return ResponseEntity.ok(
                Map.of("code",0,
                        "invitationCode",invitationCode)
        );
    }

    @GetMapping("/invite/student/me")
    public ResponseEntity<?> getStudentCode(@RequestHeader("Authorization") String authHeader,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size){
        String token = authHeader.substring(7);
        if(!authService.isTeacherTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }
        int creatorUserId = authService.getUserIdFromToken(token);

        List<InvitationCode> codes = inviteService.getMyStudentsCodes(creatorUserId, page, size);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "invitationCodes",codes)
        );
    }

    @GetMapping("/invite/student/me/notUsed")
    public ResponseEntity<?> getStudentCodeNotUsed(@RequestHeader("Authorization") String authHeader,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size){
        String token = authHeader.substring(7);
        if(!authService.isTeacherTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }
        int creatorUserId = authService.getUserIdFromToken(token);

        List<InvitationCode> codes = inviteService.getMyStudentCodesThatNotUsed(creatorUserId, page, size);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "invitationCodes",codes)
        );
    }

    @GetMapping("/invite/student/me/used")
    public ResponseEntity<?> getStudentCodeUsed(@RequestHeader("Authorization") String authHeader,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size){
        String token = authHeader.substring(7);
        if(!authService.isTeacherTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }
        int creatorUserId = authService.getUserIdFromToken(token);

        List<InvitationCode> codes = inviteService.getMyStudentCodesThatUsed(creatorUserId, page, size);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "invitationCodes",codes)
        );
    }

    @DeleteMapping("/invite/student/me/{invitationId}")
    public ResponseEntity<?> deleteStudentCode(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable Long invitationId){
        String token = authHeader.substring(7);
        if(!authService.isTeacherTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }
        if(!inviteService.isInviteCodeExist(invitationId)){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","the invite code does not exist")
            );
        }

        InvitationCode invitationCode = inviteService.getInviteCode(invitationId);
        if(invitationCode.getCodeIsUsed()){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","the invite code is used, and you can't delete it")
            );
        }

        int creatorUserId = authService.getUserIdFromToken(token);
        if(invitationCode.getCreateUserId() != creatorUserId){
            return ResponseEntity.ok(
                    Map.of("code",-4,
                            "message","you're not the creator of the code, and you can't delete it")
            );
        }

        inviteService.deleteInviteCode(invitationId);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "invitationCode",invitationCode)
        );
    }

    @GetMapping("/invite/student/info")
    public ResponseEntity<?> getMyStudentInfos(@RequestHeader("Authorization") String authHeader,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int size){
        String token = authHeader.substring(7);
        if(!authService.isTeacherTokenValid(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        List<User> students = inviteService.getMyStudentInfo(authService.getUserIdFromToken(token), page, size);
        List<UserVO> studentVOs = userService.transferUsersToUserVOs(students);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "students",studentVOs)
        );
    }
}
