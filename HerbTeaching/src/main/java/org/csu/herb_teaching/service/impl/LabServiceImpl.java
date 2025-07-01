package org.csu.herb_teaching.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.csu.herb_teaching.DTO.LabDTO;
import org.csu.herb_teaching.entity.Lab;
import org.csu.herb_teaching.mapper.LabMapper;
import org.csu.herb_teaching.mapper.CourseMapper;
import org.csu.herb_teaching.service.LabService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabServiceImpl extends ServiceImpl<LabMapper, Lab> implements LabService {

    private final LabMapper labMapper;
    private final CourseMapper courseMapper;

    @Override
    public List<Lab> getLabsByCourseId(int courseId) {
        return labMapper.selectList(new QueryWrapper<Lab>().eq("course_id", courseId).orderByAsc("lab_order"));
    }

    @Override
    public Lab getLabById(int labId) {
        return labMapper.selectById(labId);
    }

    @Override
    public Lab createLab(int courseId, LabDTO labDTO) {
        // 校验课程是否存在
        if (courseMapper.selectById(courseId) == null) {
            return null;
        }
        // 自动分配labOrder
        Integer maxOrder = labMapper.selectMaxLabOrderByCourseId(courseId);
        int nextOrder = (maxOrder == null ? 1 : maxOrder + 1);
        Lab lab = new Lab();
        BeanUtils.copyProperties(labDTO, lab);
        lab.setCourseId(courseId);
        lab.setLabOrder(nextOrder);
        labMapper.insert(lab);
        return lab;
    }

    @Override
    public Lab updateLab(int labId, LabDTO labDTO) {
        // 1. 检查实验是否存在
        Lab existingLab = labMapper.selectById(labId);
        if (existingLab == null) {
            return null; // or throw a specific exception
        }

        // 只更新非null字段
        if (labDTO.getLabName() != null) {
            existingLab.setLabName(labDTO.getLabName());
        }
        if (labDTO.getLabSteps() != null) {
            existingLab.setLabSteps(labDTO.getLabSteps());
        }
        if (labDTO.getLabOrder() != 0) { // 假如0不是合法顺序号
            existingLab.setLabOrder(labDTO.getLabOrder());
        }

        labMapper.updateById(existingLab);
        return existingLab;
    }

    @Override
    public boolean deleteLab(int labId) {
        Lab lab = labMapper.selectById(labId);
        if (lab == null) {
            return false;
        }
        labMapper.deleteById(labId);
        return true;
    }
} 