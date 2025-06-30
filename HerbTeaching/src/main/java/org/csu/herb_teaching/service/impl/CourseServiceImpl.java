package org.csu.herb_teaching.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.herb_teaching.DTO.CourseDTO;
import org.csu.herb_teaching.entity.*;
import org.csu.herb_teaching.mapper.*;
import org.csu.herb_teaching.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;
    
    @Autowired
    private LabMapper labMapper;
    
    @Autowired
    private LabInfoMapper labInfoMapper;
    
    @Autowired
    private LabResourceMapper labResourceMapper;

    // ==================== 课程管理 ====================
    @Override
    public List<Course> getAllCourses() {
        return courseMapper.selectList(null);
    }

    @Override
    public Course getCourseById(int id) {
        return courseMapper.selectById(id);
    }

    @Override
    public Course getCourseByName(String name) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_name", name);
        return courseMapper.selectOne(queryWrapper);
    }

    @Override
    public int getCourseIdByName(String courseName) {
        Course course = getCourseByName(courseName);
        return course != null ? course.getId() : -1;
    }

    @Override
    public boolean addCourse(Course course) {
        return courseMapper.insert(course) > 0;
    }

    @Override
    public boolean updateCourseForId(Course course) {
        return courseMapper.updateById(course) > 0;
    }

    @Override
    public boolean deleteCourseById(int id) {
        try {
            // 1. 获取该课程下的所有实验
            List<Lab> labs = getLabsByCourseId(id);
            
            // 2. 对每个实验，先删除其资源
            for (Lab lab : labs) {
                // 删除实验资源
                QueryWrapper<LabResource> resourceWrapper = new QueryWrapper<>();
                resourceWrapper.eq("lab_id", lab.getId());
                labResourceMapper.delete(resourceWrapper);
                
                // 删除实验详情
                QueryWrapper<LabInfo> infoWrapper = new QueryWrapper<>();
                infoWrapper.eq("lab_id", lab.getId());
                labInfoMapper.delete(infoWrapper);
            }
            
            // 3. 删除该课程下的所有实验
            QueryWrapper<Lab> labWrapper = new QueryWrapper<>();
            labWrapper.eq("course_id", id);
            labMapper.delete(labWrapper);
            
            // 4. 最后删除课程
            return courseMapper.deleteById(id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isCourseIdExist(int id) {
        return courseMapper.selectById(id) != null;
    }

    @Override
    public boolean isCourseNameExist(String name) {
        return getCourseByName(name) != null;
    }

    // ==================== 实验管理 ====================
    @Override
    public List<Lab> getAllLabs() {
        return labMapper.selectList(null);
    }

    @Override
    public List<Lab> getLabsByCourseId(int courseId) {
        QueryWrapper<Lab> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        return labMapper.selectList(queryWrapper);
    }

    @Override
    public Lab getLabById(int id) {
        return labMapper.selectById(id);
    }

    @Override
    public int getLabIdByCourseAndName(int courseId, String labName) {
        QueryWrapper<Lab> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId)
                   .eq("lab_des", labName);
        Lab lab = labMapper.selectOne(queryWrapper);
        return lab != null ? lab.getId() : -1;
    }

    @Override
    public boolean addLab(Lab lab) {
        return labMapper.insert(lab) > 0;
    }

    @Override
    public boolean updateLabForId(Lab lab) {
        return labMapper.updateById(lab) > 0;
    }

    @Override
    public boolean deleteLabById(int id) {
        try {
            // 1. 删除实验资源
            QueryWrapper<LabResource> resourceWrapper = new QueryWrapper<>();
            resourceWrapper.eq("lab_id", id);
            labResourceMapper.delete(resourceWrapper);
            
            // 2. 删除实验详情
            QueryWrapper<LabInfo> infoWrapper = new QueryWrapper<>();
            infoWrapper.eq("lab_id", id);
            labInfoMapper.delete(infoWrapper);
            
            // 3. 最后删除实验
            return labMapper.deleteById(id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isLabIdExist(int id) {
        return labMapper.selectById(id) != null;
    }

    @Override
    public boolean isLabExistInCourse(int courseId, String labName) {
        QueryWrapper<Lab> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId)
                   .eq("lab_des", labName);
        return labMapper.selectOne(queryWrapper) != null;
    }

    // ==================== 统一资源管理 ====================
    @Override
    public List<LabResource> getAllLabResources() {
        return labResourceMapper.selectList(null);
    }

    @Override
    public List<LabResource> getLabResourcesByLabId(int labId) {
        QueryWrapper<LabResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lab_id", labId)
                   .orderByAsc("lab_resource_type", "lab_resource_order");
        return labResourceMapper.selectList(queryWrapper);
    }

    @Override
    public List<LabResource> getLabResourcesByType(int labId, int resourceType) {
        QueryWrapper<LabResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lab_id", labId)
                   .eq("lab_resource_type", resourceType)
                   .orderByAsc("lab_resource_order");
        return labResourceMapper.selectList(queryWrapper);
    }

    @Override
    public LabResource getLabResourceById(int id) {
        return labResourceMapper.selectById(id);
    }

    @Override
    public boolean addLabResource(LabResource labResource) {
        return labResourceMapper.insert(labResource) > 0;
    }

    @Override
    public boolean updateLabResourceForId(LabResource labResource) {
        return labResourceMapper.updateById(labResource) > 0;
    }

    @Override
    public boolean deleteLabResourceById(int id) {
        return labResourceMapper.deleteById(id) > 0;
    }

    @Override
    public boolean isLabResourceIdExist(int id) {
        return labResourceMapper.selectById(id) != null;
    }

    // ==================== 实验详情管理 ====================
    @Override
    public List<LabInfo> getAllLabInfos() {
        return labInfoMapper.selectList(null);
    }

    @Override
    public LabInfo getLabInfoByLabId(int labId) {
        QueryWrapper<LabInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lab_id", labId);
        return labInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public LabInfo getLabInfoById(int id) {
        return labInfoMapper.selectById(id);
    }

    @Override
    public boolean addLabInfo(LabInfo labInfo) {
        return labInfoMapper.insert(labInfo) > 0;
    }

    @Override
    public boolean updateLabInfoForId(LabInfo labInfo) {
        return labInfoMapper.updateById(labInfo) > 0;
    }

    @Override
    public boolean deleteLabInfoById(int id) {
        return labInfoMapper.deleteById(id) > 0;
    }

    @Override
    public boolean isLabInfoIdExist(int id) {
        return labInfoMapper.selectById(id) != null;
    }

    @Override
    public boolean isLabInfoExistForLab(int labId) {
        QueryWrapper<LabInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lab_id", labId);
        return labInfoMapper.selectOne(queryWrapper) != null;
    }

    // ==================== 药材关联查询 ====================
    @Override
    public List<Integer> getHerbsOnLab(int labId) {
        QueryWrapper<LabInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lab_id", labId)
                   .select("herb_id");
        List<LabInfo> labInfos = labInfoMapper.selectList(queryWrapper);
        return labInfos.stream()
                      .mapToInt(LabInfo::getHerbId)
                      .boxed()
                      .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Integer> getLabsOnHerb(int herbId) {
        QueryWrapper<LabInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("herb_id", herbId)
                   .select("lab_id");
        List<LabInfo> labInfos = labInfoMapper.selectList(queryWrapper);
        return labInfos.stream()
                      .mapToInt(LabInfo::getLabId)
                      .boxed()
                      .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean isExistHerbOnLab(int labId) {
        QueryWrapper<LabInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("lab_id", labId);
        return labInfoMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public boolean isExistLabOnHerb(int herbId) {
        QueryWrapper<LabInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("herb_id", herbId);
        return labInfoMapper.selectCount(queryWrapper) > 0;
    }

    // ==================== 分类查询 ====================
    @Override
    public List<Course> getCoursesByType(String courseType) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_type", courseType);
        return courseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Course> getCoursesByObject(String courseObject) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_object", courseObject);
        return courseMapper.selectList(queryWrapper);
    }

    // ==================== 搜索功能 ====================
    @Override
    public List<Course> searchCourses(String keyword, String courseType, String courseObject) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();

        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper.like("course_name", keyword)
                    .or()
                    .like("course_des", keyword));
        }

        if (courseType != null && !courseType.isEmpty()) {
            queryWrapper.eq("course_type", courseType);
        }

        if (courseObject != null && !courseObject.isEmpty()) {
            queryWrapper.eq("course_object", courseObject);
        }

        return courseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Lab> searchLabsByKeyword(String keyword) {
        QueryWrapper<Lab> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("lab_des", keyword);
        return labMapper.selectList(queryWrapper);
    }

    // ==================== 资源管理 ====================
    @Override
    public String getResourcePreviewUrl(int resourceId, String resourceType) {
        return "/preview/" + resourceType + "/" + resourceId;
    }

    @Override
    public String getResourceDownloadUrl(int resourceId, String resourceType) {
        return "/download/" + resourceType + "/" + resourceId;
    }

    @Override
    public boolean isResourceExist(int resourceId, String resourceType) {
        return isLabResourceIdExist(resourceId);
    }

    // ==================== DTO转换 ====================
    @Override
    public Course transferDTOToCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setName(courseDTO.getCourseName());
        course.setStartTime(new Timestamp(courseDTO.getCourseStartTime().getTime()));
        course.setEndTime(new Timestamp(courseDTO.getCourseEndTime().getTime()));
        course.setDes(courseDTO.getCourseDes());
        course.setCourseType(courseDTO.getCourseType());
        course.setCourseObject(courseDTO.getCourseObject());
        return course;
    }

    @Override
    public CourseDTO transferCourseToDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName(course.getName());
        courseDTO.setCourseStartTime(new Date(course.getStartTime().getTime()));
        courseDTO.setCourseEndTime(new Date(course.getEndTime().getTime()));
        courseDTO.setCourseDes(course.getDes());
        courseDTO.setCourseType(course.getCourseType());
        courseDTO.setCourseObject(course.getCourseObject());
        return courseDTO;
    }
}
