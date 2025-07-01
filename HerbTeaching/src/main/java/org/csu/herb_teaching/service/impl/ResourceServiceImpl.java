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
        CourseResource resource = new CourseResource();
        BeanUtils.copyProperties(resourceDTO, resource);
        resource.setCourseId(courseId);
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

        existingResource.setCourseResourceType(resourceDTO.getCourseResourceType());
        existingResource.setCourseResourceOrder(resourceDTO.getCourseResourceOrder());
        existingResource.setCourseResourceTitle(resourceDTO.getCourseResourceTitle());
        existingResource.setCourseResourceContent(resourceDTO.getCourseResourceContent());
        existingResource.setCourseResourceMetadata(resourceDTO.getCourseResourceMetadata());

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