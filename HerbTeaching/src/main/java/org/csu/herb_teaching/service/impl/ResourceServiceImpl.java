package org.csu.herb_teaching.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.csu.herb_teaching.DTO.CourseResourceDTO;
import org.csu.herb_teaching.entity.CourseResource;
import org.csu.herb_teaching.mapper.CourseResourceMapper;
import org.csu.herb_teaching.mapper.CourseMapper;
import org.csu.herb_teaching.service.ResourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl extends ServiceImpl<CourseResourceMapper, CourseResource> implements ResourceService {

    private final CourseResourceMapper courseResourceMapper;
    private final CourseMapper courseMapper;

    @Override
    public List<CourseResource> getResourcesByCourseId(int courseId) {
        return courseResourceMapper.selectList(new QueryWrapper<CourseResource>().eq("course_id", courseId).orderByAsc("course_resource_order"));
    }

    @Override
    public CourseResource getResourceById(int resourceId) {
        return courseResourceMapper.selectById(resourceId);
    }

    @Override
    public CourseResource createResource(int courseId, CourseResourceDTO resourceDTO) {
        // 校验课程是否存在
        if (courseMapper.selectById(courseId) == null) {
            return null;
        }
        // 自动分配courseResourceOrder
        Integer maxOrder = courseResourceMapper.selectMaxCourseResourceOrderByCourseId(courseId);
        int nextOrder = (maxOrder == null ? 1 : maxOrder + 1);
        CourseResource resource = new CourseResource();
        resource.setCourseId(courseId);
        resource.setCourseResourceType(resourceDTO.getCourseResourceType());
        resource.setCourseResourceTitle(resourceDTO.getCourseResourceTitle());
        resource.setCourseResourceContent(resourceDTO.getCourseResourceContent());
        resource.setCourseResourceMetadata(resourceDTO.getCourseResourceMetadata());
        resource.setCourseResourceOrder(nextOrder);
        resource.setCourseResourceTime(LocalDateTime.now());
        resource.setCourseResourceIsvalid(true);
        courseResourceMapper.insert(resource);
        return resource;
    }

    @Override
    public CourseResource updateResource(int resourceId, CourseResourceDTO resourceDTO) {
        CourseResource existingResource = courseResourceMapper.selectById(resourceId);
        if (existingResource == null) {
            return null;
        }

        // 只更新前端传了的字段
        if (resourceDTO.getCourseResourceType() != null) {
            existingResource.setCourseResourceType(resourceDTO.getCourseResourceType());
        }
        if (resourceDTO.getCourseResourceOrder() != null) {
            existingResource.setCourseResourceOrder(resourceDTO.getCourseResourceOrder());
        }
        if (resourceDTO.getCourseResourceTitle() != null) {
            existingResource.setCourseResourceTitle(resourceDTO.getCourseResourceTitle());
        }
        if (resourceDTO.getCourseResourceContent() != null) {
            existingResource.setCourseResourceContent(resourceDTO.getCourseResourceContent());
        }
        if (resourceDTO.getCourseResourceMetadata() != null) {
            existingResource.setCourseResourceMetadata(resourceDTO.getCourseResourceMetadata());
        }

        courseResourceMapper.updateById(existingResource);
        return existingResource;
    }

    @Override
    public boolean deleteResource(int resourceId) {
        CourseResource resource = courseResourceMapper.selectById(resourceId);
        if (resource == null) {
            return false;
        }
        courseResourceMapper.deleteById(resourceId);
        return true;
    }
} 