package org.csu.hisuser.service;

import org.csu.hisuser.entity.InvitationCode;
import org.csu.hisuser.entity.User;
import org.csu.hisuser.entity.UserLinkInvitation;

import java.util.List;

public interface InviteService {
    InvitationCode generateTeacherInviteCode(int creatByUserId, String schoolName, String teacherName);
    InvitationCode generateStudentInviteCode(int creatByUserId, String schoolName, String studentName);

    boolean deleteInviteCode(Long inviteCodeId);
    Long getInvitationCodeIdByCode(String inviteCode);
    InvitationCode getInviteCode(Long inviteCodeId);
    List<InvitationCode> getMyStudentsCodes(int userId,int page,int size);
    List<InvitationCode> getMyStudentCodesThatNotUsed(int userId,int page,int size);
    List<InvitationCode> getMyStudentCodesThatUsed(int userId,int page,int size);
    List<User> getMyStudentInfo(int userId,int page,int size);
    List<InvitationCode> getAllInviteCodes(int page,int size);
    List<InvitationCode> getAllInviteCodesThatNotUsed(int page,int size);
    List<InvitationCode> getAllInviteCodesThatUsed(int page,int size);
    boolean isInviteCodeExist(Long inviteCodeId);

    UserLinkInvitation addLinkInvitation(int userId,Long inviteCodeId);
    UserLinkInvitation getLinkInvitationByUserId(int userId);
    UserLinkInvitation getLinkInvitationByInviteCode(Long inviteCodeId);
    String getSchoolNameByUserId(int userId);
    boolean deleteLinkInvitation(int userId,Long inviteCodeId);

    //0表示成功 -1邀请码不正确 -2注册类型和邀请码类型不符 -3学校名不正确 -4教师名不正确 -5邀请码已被使用 -6邀请码过期
    int useInviteCode(String inviteCode,int categoryId,String schoolName, String userName);
}
