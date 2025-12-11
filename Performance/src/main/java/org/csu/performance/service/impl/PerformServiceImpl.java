package org.csu.performance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.csu.performance.DTO.PerformAuditDTO;
import org.csu.performance.DTO.PerformDTO;
import org.csu.performance.VO.PageResultVO;
import org.csu.performance.VO.PerformFileVO;
import org.csu.performance.VO.PerformVO;
import org.csu.performance.VO.ResultVO;
import org.csu.performance.entity.Perform;
import org.csu.performance.entity.PerformType;
import org.csu.performance.feign.UserFeignClient;
import org.csu.performance.mapper.PerformFileMapper;
import org.csu.performance.mapper.PerformMapper;
import org.csu.performance.mapper.PerformTypeMapper;
import org.csu.performance.service.PerformService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 业绩Service实现类
 */
@Service
public class PerformServiceImpl implements PerformService {

    @Autowired
    private PerformMapper performMapper;

    @Autowired
    private PerformTypeMapper performTypeMapper;
    
    @Autowired
    private UserFeignClient userFeignClient;
    
    @Autowired
    private PerformFileMapper performFileMapper;

    @Override
    @Transactional
    public ResultVO<PerformVO> createPerform(PerformDTO performDTO, Long submitUserId) {
        // 验证业绩类型是否存在
        PerformType performType = performTypeMapper.selectById(performDTO.getPerformTypeId());
        if (performType == null || !performType.getIsActive()) {
            return ResultVO.error("业绩类型不存在或已禁用");
        }

        // 检查业绩名称是否已存在
        Integer count = performMapper.checkPerformNameExists(performDTO.getPerformName(), null);
        if (count > 0) {
            return ResultVO.error("业绩名称已存在");
        }

        // 创建业绩实体
        Perform perform = new Perform();
        BeanUtils.copyProperties(performDTO, perform);
        perform.setSubmitUserId(submitUserId);
        perform.setPerformStatus(0); // 草稿状态
        perform.setPerformTime(performDTO.getPerformTime() != null ? performDTO.getPerformTime() : LocalDateTime.now());
        


        // 保存业绩
        performMapper.insert(perform);

        // 返回业绩详情
        return getPerformById(perform.getPerformId());
    }

    @Override
    public ResultVO<PageResultVO<PerformVO>> getPerformList(String keyword, Long performTypeId, 
                                                           Integer performStatus, Integer page, Integer size) {
        Page<PerformVO> pageParam = new Page<>(page, size);
        IPage<PerformVO> pageResult = performMapper.selectPerformPage(pageParam, keyword, performTypeId, performStatus);
        
        // 填充用户信息
        fillUserInfo(pageResult.getRecords());
        
        PageResultVO<PerformVO> result = new PageResultVO<>(
            pageResult.getTotal(), 
            pageResult.getPages(), 
            pageResult.getRecords()
        );
        
        return ResultVO.success("业绩列表获取成功", result);
    }

    @Override
    public ResultVO<PageResultVO<PerformVO>> getPerformListExcludeDraft(String keyword, Long performTypeId, 
                                                                       Integer performStatus, Integer page, Integer size) {
        Page<PerformVO> pageParam = new Page<>(page, size);
        IPage<PerformVO> pageResult = performMapper.selectPerformPageExcludeDraft(pageParam, keyword, performTypeId, performStatus);
        
        // 填充用户信息
        fillUserInfo(pageResult.getRecords());
        
        PageResultVO<PerformVO> result = new PageResultVO<>(
            pageResult.getTotal(), 
            pageResult.getPages(), 
            pageResult.getRecords()
        );
        
        return ResultVO.success("业绩列表获取成功", result);
    }

    @Override
    public ResultVO<PerformVO> getPerformById(Long performId) {
        // 检查业绩是否存在
        if (!checkPerformExists(performId)) {
            return ResultVO.error("业绩不存在");
        }

        PerformVO performVO = performMapper.selectPerformDetailById(performId);
        if (performVO == null) {
            return ResultVO.error("业绩不存在");
        }

        // 通过Feign调用获取提交人信息
        if (performVO.getSubmitUserId() != null) {
            try {
                Map<String, Object> userInfo = userFeignClient.getUserInfoById(performVO.getSubmitUserId().intValue());
                if (userInfo != null && userInfo.get("username") != null) {
                    performVO.setSubmitUserName((String) userInfo.get("username"));
                    // 根据用户类型设置角色
                    if (userFeignClient.isUserAdmin(performVO.getSubmitUserId().intValue())) {
                        performVO.setSubmitUserRole("管理员");
                    } else if (userFeignClient.isUserRealTeacher(performVO.getSubmitUserId().intValue())) {
                        performVO.setSubmitUserRole("教师");
                    } else {
                        performVO.setSubmitUserRole("学生");
                    }
                }
            } catch (Exception e) {
                // 如果获取用户信息失败，不影响业绩信息的返回
                performVO.setSubmitUserName("未知用户");
                performVO.setSubmitUserRole("未知角色");
            }
        }

        // 查询并填充附件信息
        List<PerformFileVO> files = performFileMapper.selectFilesByPerformId(performId);
        performVO.setFiles(files);
        performVO.setFileCount(files != null ? files.size() : 0);

        return ResultVO.success("业绩详情获取成功", performVO);
    }

    @Override
    @Transactional
    public ResultVO<PerformVO> updatePerform(Long performId, PerformDTO performDTO, Long submitUserId) {
        // 检查业绩是否存在
        if (!checkPerformExists(performId)) {
            return ResultVO.error("业绩不存在");
        }

        // 检查用户权限
        if (!checkUserPermission(performId, submitUserId)) {
            return ResultVO.error("无权限操作此业绩");
        }

        Perform perform = performMapper.selectById(performId);
        if (perform == null) {
            return ResultVO.error("业绩不存在");
        }

        // 只有草稿状态的业绩可以编辑
        if (perform.getPerformStatus() != 0) {
            return ResultVO.error("只有草稿状态的业绩可以编辑");
        }

        // 验证业绩类型是否存在（如果提供了的话）
        if (performDTO.getPerformTypeId() != null) {
            PerformType performType = performTypeMapper.selectById(performDTO.getPerformTypeId());
            if (performType == null || !performType.getIsActive()) {
                return ResultVO.error("业绩类型不存在或已禁用");
            }
        }

        // 检查业绩名称是否已存在（排除当前业绩）
        if (StringUtils.hasText(performDTO.getPerformName())) {
            Integer count = performMapper.checkPerformNameExists(performDTO.getPerformName(), performId);
            if (count > 0) {
                return ResultVO.error("业绩名称已存在");
            }
        }

        // 只更新用户实际提交的字段，未提交的字段保留原值
        boolean hasUpdate = false;
        
        if (StringUtils.hasText(performDTO.getPerformName())) {
            perform.setPerformName(performDTO.getPerformName());
            hasUpdate = true;
        }
        
        if (StringUtils.hasText(performDTO.getPerformContent())) {
            perform.setPerformContent(performDTO.getPerformContent());
            hasUpdate = true;
        }
        
        if (performDTO.getPerformTypeId() != null) {
            perform.setPerformTypeId(performDTO.getPerformTypeId());
            hasUpdate = true;
        }
        
        if (performDTO.getPerformTime() != null) {
            perform.setPerformTime(performDTO.getPerformTime());
            hasUpdate = true;
        }
        
        // 如果有字段更新，则保存
        if (hasUpdate) {
            performMapper.updateById(perform);
        }

        // 返回更新后的业绩详情
        return getPerformById(performId);
    }

    @Override
    @Transactional
    public ResultVO<PerformVO> submitPerform(Long performId, Long submitUserId) {
        // 检查业绩是否存在
        if (!checkPerformExists(performId)) {
            return ResultVO.error("业绩不存在");
        }

        // 检查用户权限
        if (!checkUserPermission(performId, submitUserId)) {
            return ResultVO.error("无权限操作此业绩");
        }

        Perform perform = performMapper.selectById(performId);
        if (perform == null) {
            return ResultVO.error("业绩不存在");
        }

        // 只有草稿状态的业绩可以提交
        if (perform.getPerformStatus() != 0) {
            return ResultVO.error("只有草稿状态的业绩可以提交");
        }

        // 更新业绩状态为已提交
        perform.setPerformStatus(1);
        perform.setPerformTime(LocalDateTime.now());
        performMapper.updateById(perform);

        // 返回更新后的业绩详情
        return getPerformById(performId);
    }

    @Override
    @Transactional
    public ResultVO<Void> deletePerform(Long performId, Long submitUserId) {
        // 检查业绩是否存在
        if (!checkPerformExists(performId)) {
            return ResultVO.error("业绩不存在");
        }

        // 检查用户权限
        if (!checkUserPermission(performId, submitUserId)) {
            return ResultVO.error("无权限操作此业绩");
        }

        Perform perform = performMapper.selectById(performId);
        if (perform == null) {
            return ResultVO.error("业绩不存在");
        }

        // 只有草稿状态的业绩可以删除
        if (perform.getPerformStatus() != 0) {
            return ResultVO.error("只有草稿状态的业绩可以删除");
        }

        // 删除业绩（附件会通过外键级联删除）
        performMapper.deleteById(performId);

        return ResultVO.success("业绩删除成功");
    }

    @Override
    public ResultVO<PageResultVO<PerformVO>> getPendingPerformList(Integer page, Integer size) {
        Page<PerformVO> pageParam = new Page<>(page, size);
        IPage<PerformVO> pageResult = performMapper.selectPendingPerformPage(pageParam);
        
        // 填充用户信息
        fillUserInfo(pageResult.getRecords());
        
        PageResultVO<PerformVO> result = new PageResultVO<>(
            pageResult.getTotal(), 
            pageResult.getPages(), 
            pageResult.getRecords()
        );
        
        return ResultVO.success("待审核业绩列表获取成功", result);
    }

    @Override
    @Transactional
    public ResultVO<PerformVO> auditPerform(Long performId, PerformAuditDTO auditDTO, Long auditUserId) {
        // 检查业绩是否存在
        if (!checkPerformExists(performId)) {
            return ResultVO.error("业绩不存在");
        }

        Perform perform = performMapper.selectById(performId);
        if (perform == null) {
            return ResultVO.error("业绩不存在");
        }

        // 只有已提交状态的业绩可以审核
        if (perform.getPerformStatus() != 1) {
            return ResultVO.error("只有已提交状态的业绩可以审核");
        }

        // 验证审核结果
        if (auditDTO.getAuditResult() != 2 && auditDTO.getAuditResult() != 3) {
            return ResultVO.error("审核结果无效");
        }

        // 更新业绩状态
        perform.setPerformStatus(auditDTO.getAuditResult());
        perform.setPerformComment(auditDTO.getPerformComment());
        perform.setAuditTime(LocalDateTime.now());
        perform.setAuditBy(auditUserId);
        performMapper.updateById(perform);

        // 返回更新后的业绩详情
        return getPerformById(performId);
    }

    @Override
    public ResultVO<PageResultVO<PerformVO>> getMyPerformList(Long submitUserId, Integer page, Integer size) {
        Page<PerformVO> pageParam = new Page<>(page, size);
        IPage<PerformVO> pageResult = performMapper.selectMyPerformPage(pageParam, submitUserId);
        
        // 填充用户信息
        fillUserInfo(pageResult.getRecords());
        
        PageResultVO<PerformVO> result = new PageResultVO<>(
            pageResult.getTotal(), 
            pageResult.getPages(), 
            pageResult.getRecords()
        );
        
        return ResultVO.success("我的业绩列表获取成功", result);
    }

    @Override
    public boolean checkPerformExists(Long performId) {
        if (performId == null) {
            return false;
        }
        return performMapper.selectById(performId) != null;
    }

    @Override
    public boolean checkUserPermission(Long performId, Long userId) {
        if (performId == null || userId == null) {
            return false;
        }
        Perform perform = performMapper.selectById(performId);
        return perform != null && perform.getSubmitUserId().equals(userId);
    }

    /**
     * 填充用户信息
     * @param performVOList 业绩VO列表
     */
    private void fillUserInfo(List<PerformVO> performVOList) {
        if (performVOList == null || performVOList.isEmpty()) {
            return;
        }
        
        // 收集所有需要查询的用户ID
        List<Integer> userIdList = performVOList.stream()
            .map(vo -> vo.getSubmitUserId())
            .filter(id -> id != null)
            .map(id -> id.intValue())
            .distinct()
            .toList();
        
        if (userIdList.isEmpty()) {
            return;
        }
        
        try {
            // 批量获取用户信息
            Map<Integer, Map<String, Object>> userMap = userFeignClient.getUserInfoBatch(userIdList);
            if (userMap != null) {
                // 填充每个业绩的用户信息
                for (PerformVO performVO : performVOList) {
                    if (performVO.getSubmitUserId() != null) {
                        Map<String, Object> userInfo = userMap.get(performVO.getSubmitUserId().intValue());
                        if (userInfo != null && userInfo.get("username") != null) {
                            performVO.setSubmitUserName((String) userInfo.get("username"));
                            // 根据用户类型设置角色
                            if (userFeignClient.isUserAdmin(performVO.getSubmitUserId().intValue())) {
                                performVO.setSubmitUserRole("管理员");
                            } else if (userFeignClient.isUserRealTeacher(performVO.getSubmitUserId().intValue())) {
                                performVO.setSubmitUserRole("教师");
                            } else {
                                performVO.setSubmitUserRole("学生");
                            }
                        } else {
                            performVO.setSubmitUserName("未知用户");
                            performVO.setSubmitUserRole("未知角色");
                        }
                    }
                }
            }
        } catch (Exception e) {
            // 如果获取用户信息失败，设置默认值
            for (PerformVO performVO : performVOList) {
                performVO.setSubmitUserName("未知用户");
                performVO.setSubmitUserRole("未知角色");
            }
        }
    }
} 