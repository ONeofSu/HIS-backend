package org.csu.hisuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.csu.hisuser.entity.InvitationCode;
import org.csu.hisuser.entity.User;
import org.csu.hisuser.entity.UserLinkInvitation;
import org.csu.hisuser.mapper.InvitationCodeMapper;
import org.csu.hisuser.mapper.UserLinkInvitationMapper;
import org.csu.hisuser.service.InviteService;
import org.csu.hisuser.service.UserService;
import org.csu.hisuser.util.InvitationCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InviteServiceImpl implements InviteService {
    @Autowired
    InvitationCodeMapper invitationCodeMapper;
    @Autowired
    UserService userService;
    @Autowired
    UserLinkInvitationMapper userLinkInvitationMapper;

    private InvitationCode getInvitationCodeByCode(String code){
        QueryWrapper<InvitationCode> wrapper = new QueryWrapper<>();
        wrapper.eq("code", code);
        return invitationCodeMapper.selectOne(wrapper);
    }

    @Override
    public InvitationCode generateTeacherInviteCode(int creatByUserId, String schoolName, String teacherName) {
        InvitationCode invitationCode = new InvitationCode();

        String inviteCode = InvitationCodeUtil.generateInvitationCode();
        invitationCode.setCode(inviteCode);
        invitationCode.setCategoryId(2);
        invitationCode.setCreateUserId(creatByUserId);
        invitationCode.setInviteSchool(schoolName);
        invitationCode.setInviteName(teacherName);
        invitationCode.setCodeIsUsed(false);
        invitationCode.setCodeExpireTime(LocalDateTime.now().plusDays(1));  //有效期一天

        invitationCodeMapper.insert(invitationCode);
        return invitationCode;
    }

    @Override
    public InvitationCode generateStudentInviteCode(int creatByUserId, String schoolName, String studentName) {
        InvitationCode invitationCode = new InvitationCode();

        String inviteCode = InvitationCodeUtil.generateInvitationCode();
        invitationCode.setCode(inviteCode);
        invitationCode.setCategoryId(1);
        invitationCode.setCreateUserId(creatByUserId);
        invitationCode.setInviteSchool(schoolName);
        invitationCode.setInviteName(studentName);
        invitationCode.setCodeIsUsed(false);
        invitationCode.setCodeExpireTime(LocalDateTime.now().plusDays(1));  //有效期一天

        invitationCodeMapper.insert(invitationCode);
        return invitationCode;
    }

    @Override
    public Long getInvitationCodeIdByCode(String inviteCode) {
        QueryWrapper<InvitationCode> wrapper = new QueryWrapper<>();
        wrapper.eq("code", inviteCode);
        return invitationCodeMapper.selectOne(wrapper).getCodeId();
    }

    @Override
    public boolean deleteInviteCode(Long inviteCodeId) {
        invitationCodeMapper.deleteById(inviteCodeId);
        return true;
    }

    @Override
    public InvitationCode getInviteCode(Long inviteCodeId) {
        return invitationCodeMapper.selectById(inviteCodeId);
    }

    @Override
    public boolean isInviteCodeExist(Long inviteCodeId) {
        return invitationCodeMapper.selectById(inviteCodeId) != null;
    }

    @Override
    public List<InvitationCode> getMyStudentsCodes(int userId, int page, int size) {
        Page<InvitationCode> pageParam = new Page<>(page, size);
        QueryWrapper<InvitationCode> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("category_id", 1);
        return invitationCodeMapper.selectPage(pageParam,wrapper).getRecords();
    }

    @Override
    public List<InvitationCode> getMyStudentCodesThatNotUsed(int userId, int page, int size) {
        Page<InvitationCode> pageParam = new Page<>(page, size);
        QueryWrapper<InvitationCode> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("code_is_used", false).eq("category_id",1);
        return invitationCodeMapper.selectPage(pageParam,wrapper).getRecords();
    }

    @Override
    public List<InvitationCode> getMyStudentCodesThatUsed(int userId, int page, int size) {
        Page<InvitationCode> pageParam = new Page<>(page, size);
        QueryWrapper<InvitationCode> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("code_is_used", true).eq("category_id",1);
        return invitationCodeMapper.selectPage(pageParam,wrapper).getRecords();
    }

    @Override
    public List<User> getMyStudentInfo(int userId, int page, int size) {
        List<InvitationCode> invitationCodes = getMyStudentCodesThatUsed(userId, page, size);//获得已被使用的邀请码
        List<User> students = new ArrayList<>();
        for (InvitationCode invitationCode : invitationCodes) {
            UserLinkInvitation userLinkInvitation = getLinkInvitationByInviteCode(invitationCode.getCodeId());
            User user = userService.getUserById(userLinkInvitation.getUserId());
            students.add(user);
        }
        return students;
    }

    @Override
    public List<InvitationCode> getAllInviteCodes(int page,int size) {
        Page<InvitationCode> pageParma = new Page<>(page,size);
        return invitationCodeMapper.selectPage(pageParma,null).getRecords();
    }

    @Override
    public List<InvitationCode> getAllInviteCodesThatNotUsed(int page,int size) {
        Page<InvitationCode> pageParma = new Page<>(page,size);
        QueryWrapper<InvitationCode> wrapper = new QueryWrapper<>();
        wrapper.eq("code_is_used", false);
        return invitationCodeMapper.selectPage(pageParma,wrapper).getRecords();
    }

    @Override
    public List<InvitationCode> getAllInviteCodesThatUsed(int page,int size) {
        Page<InvitationCode> pageParma = new Page<>(page,size);
        QueryWrapper<InvitationCode> wrapper = new QueryWrapper<>();
        wrapper.eq("code_is_used", true);
        return invitationCodeMapper.selectPage(pageParma,wrapper).getRecords();
    }

    @Override
    public UserLinkInvitation addLinkInvitation(int userId, Long inviteCodeId) {
        UserLinkInvitation userLinkInvitation = new UserLinkInvitation();
        userLinkInvitation.setUserId(userId);
        userLinkInvitation.setCodeId(inviteCodeId);
        userLinkInvitationMapper.insert(userLinkInvitation);
        return userLinkInvitation;
    }

    @Override
    public UserLinkInvitation getLinkInvitationByUserId(int userId) {
        QueryWrapper<UserLinkInvitation> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return userLinkInvitationMapper.selectOne(wrapper);
    }

    @Override
    public UserLinkInvitation getLinkInvitationByInviteCode(Long inviteCodeId) {
        QueryWrapper<UserLinkInvitation> wrapper = new QueryWrapper<>();
        wrapper.eq("code_id", inviteCodeId);
        return userLinkInvitationMapper.selectOne(wrapper);
    }

    @Override
    public String getSchoolNameByUserId(int userId) {
        UserLinkInvitation userLinkInvitation = getLinkInvitationByUserId(userId);
        InvitationCode invitationCode = getInviteCode(userLinkInvitation.getCodeId());
        return invitationCode.getInviteSchool();
    }

    @Override
    public boolean deleteLinkInvitation(int userId, Long inviteCodeId) {
        QueryWrapper<UserLinkInvitation> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("invitation_code_id", inviteCodeId);
        userLinkInvitationMapper.delete(wrapper);
        return true;
    }

    @Override
    public int useInviteCode(String inviteCode,int categoryId, String schoolName, String userName) {
        InvitationCode invitationCode = getInvitationCodeByCode(inviteCode);
        if( invitationCode == null ){
            return -1;
        }
        if(invitationCode.getCategoryId()!=categoryId){
            return -2;
        }
        if(!invitationCode.getInviteSchool().equals(schoolName)){
            return -3;
        }
        if(!invitationCode.getInviteName().equals(userName)){
            return -4;
        }
        if(invitationCode.getCodeIsUsed()){
            return -5;
        }
        if(invitationCode.getCodeExpireTime().isBefore(LocalDateTime.now())){
            return -6;
        }
        invitationCode.setCodeIsUsed(true);

        invitationCodeMapper.updateById(invitationCode);
        return 0;
    }
}
