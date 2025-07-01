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
        Lab lab = new Lab();
        BeanUtils.copyProperties(labDTO, lab);
        lab.setCourseId(courseId);
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

        // 2. 手动将DTO中的非空属性复制到已存在的实体上
        existingLab.setLabName(labDTO.getLabName());
        existingLab.setLabSteps(labDTO.getLabSteps());
        existingLab.setLabOrder(labDTO.getLabOrder());

        // 3. 执行更新操作并返回更新后的对象
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