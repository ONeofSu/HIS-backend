package org.csu.hisuser.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.csu.hisuser.DTO.BatchInviteDTO;
import org.csu.hisuser.VO.BatchInviteResultVO;
import org.csu.hisuser.config.ThreadPoolConfig;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class InviteServiceImpl implements InviteService {
    @Autowired
    InvitationCodeMapper invitationCodeMapper;
    @Autowired
    UserService userService;
    @Autowired
    UserLinkInvitationMapper userLinkInvitationMapper;

    private final ExecutorService inviteCodeExecutor;

    public InviteServiceImpl(ExecutorService inviteCodeExecutor) {
        this.inviteCodeExecutor = inviteCodeExecutor;
    }

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

    // todo 看看可不可以用future

    @Override
    @Transactional
    public List<BatchInviteResultVO> bathInvite(int creatorUserId, MultipartFile file) {
        // 1. 读取Excel数据
        List<BatchInviteDTO> inviteList = readExcelData(file);

        // 2. 验证创建者权限和学校信息
        String creatorSchool = getCreatorSchool(creatorUserId);

        // 3. 准备线程安全的结果集合
        List<BatchInviteResultVO> results = Collections.synchronizedList(new ArrayList<>());

        // 4. 使用CountDownLatch等待所有任务完成
        CountDownLatch latch = new CountDownLatch(inviteList.size());

        // 5. 提交任务到线程池
        for (BatchInviteDTO dto : inviteList) {
            inviteCodeExecutor.submit(() -> {
                try {
                    processSingleInvite(creatorUserId, creatorSchool, dto, results);
                } finally {
                    latch.countDown();
                }
            });
        }

        // 6. 等待所有任务完成
        try {
            latch.await(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("批量处理被中断");
        }

        return results;
    }

    // todo 完善权限检查逻辑
    /**
     * 处理邀请
     * @param creatorUserId
     * @param creatorSchool
     * @param dto
     * @param results
     */
    private void processSingleInvite(int creatorUserId, String creatorSchool,
                                     BatchInviteDTO dto, List<BatchInviteResultVO> results) {
        try {
            // 验证学校是否匹配
            if (userService.getCategoryOnUser(creatorUserId).getId() == 2 &&
                    !creatorSchool.equals(dto.getSchoolName())) {
                results.add(new BatchInviteResultVO(
                        dto.getUserName(), dto.getSchoolName(), dto.getInviteCodeType(),
                        false, "学校不匹配", null));
                return;
            }

            // 根据类别生成邀请码
            InvitationCode code;
            if (dto.getInviteCodeType() == 1) {
                code = generateStudentInviteCode(creatorUserId, dto.getSchoolName(), dto.getUserName());
            } else if (dto.getInviteCodeType() == 2) {
                code = generateTeacherInviteCode(creatorUserId, dto.getSchoolName(), dto.getUserName());
            } else {
                results.add(new BatchInviteResultVO(
                        dto.getUserName(), dto.getSchoolName(), dto.getInviteCodeType(),
                        false, "无效的用户类别", null));
                return;
            }

            results.add(new BatchInviteResultVO(
                    dto.getUserName(), dto.getSchoolName(), dto.getInviteCodeType(),
                    true, "生成成功", code.getCode()));
        } catch (Exception e) {
            results.add(new BatchInviteResultVO(
                    dto.getUserName(), dto.getSchoolName(), dto.getInviteCodeType(),
                    false, e.getMessage(), null));
        }
    }

    /**
     * 获得邀请发起者学校
     * @param creatorUserId
     * @return 邀请发起者的学校名
     */
    private String getCreatorSchool(int creatorUserId) {
        if (userService.getCategoryOnUser(creatorUserId).getId() == 2) {
            String school = this.getSchoolNameByUserId(creatorUserId);
            if (school == null || school.isEmpty()) {
                throw new RuntimeException("教师学校信息不存在");
            }
            return school;
        }
        return null; // 管理员可以创建任意学校的邀请码
    }

    /**
     * 解析Excel内容
     * @param file
     * @return 解析后的DTO对象
     */
    private List<BatchInviteDTO> readExcelData(MultipartFile file) {
        // 使用EasyExcel读取Excel
        try {
            return EasyExcel.read(file.getInputStream())
                    .head(BatchInviteDTO.class)
                    .sheet()
                    .doReadSync();
        } catch (IOException e) {
            throw new RuntimeException("读取Excel文件失败", e);
        }
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
