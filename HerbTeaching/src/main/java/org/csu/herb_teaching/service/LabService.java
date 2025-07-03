package org.csu.herb_teaching.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.csu.herb_teaching.DTO.LabDTO;
import org.csu.herb_teaching.entity.Lab;

import java.util.List;

public interface LabService extends IService<Lab> {
    List<Lab> getLabsByCourseId(int courseId);
    Lab getLabById(int labId);
    Lab createLab(int courseId, LabDTO labDTO);
    Lab updateLab(int labId, LabDTO labDTO);
    boolean deleteLab(int labId);
} 