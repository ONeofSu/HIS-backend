package org.csu.herb_teaching.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.csu.herb_teaching.DTO.CourseResourceDTO;
import org.csu.herb_teaching.entity.CourseResource;

import java.util.List;

public interface ResourceService extends IService<CourseResource> {
    List<CourseResource> getResourcesByCourseId(int courseId);
    CourseResource getResourceById(int resourceId);
    CourseResource createResource(int courseId, CourseResourceDTO resourceDTO);
    CourseResource updateResource(int resourceId, CourseResourceDTO resourceDTO);
    boolean deleteResource(int resourceId);
} 