package org.csu.herb_teaching.service;

import org.csu.herb_teaching.DTO.CourseDTO;
import org.csu.herb_teaching.entity.*;

import java.util.List;

public interface CourseService {
    // ==================== 课程管理 ====================
    public List<Course> getAllCourses();
    public Course getCourseById(int id);    //空就返回NULL
    public Course getCourseByName(String name); //空就返回NULL
    public int getCourseIdByName(String courseName);    //空返回-1
    public boolean addCourse(Course course);
    public boolean updateCourseForId(Course course);  //在对应位置上修改id
    public boolean deleteCourseById(int id);  //只在形式上删除

    public boolean isCourseIdExist(int id);
    public boolean isCourseNameExist(String name);

    // ==================== 实验管理 ====================
    public List<Lab> getAllLabs();
    public List<Lab> getLabsByCourseId(int courseId);  //获取课程下的所有实验
    public Lab getLabById(int id);    //空就返回NULL
    public int getLabIdByCourseAndName(int courseId, String labName);    //空返回-1
    public boolean addLab(Lab lab);
    public boolean updateLabForId(Lab lab);  //在对应位置上修改id
    public boolean deleteLabById(int id);  //只在形式上删除

    public boolean isLabIdExist(int id);
    public boolean isLabExistInCourse(int courseId, String labName);

    // ==================== 统一资源管理 ====================
    public List<LabResource> getAllLabResources();
    public List<LabResource> getLabResourcesByLabId(int labId);  //获取实验的所有资源
    public List<LabResource> getLabResourcesByType(int labId, int resourceType);  //获取实验的特定类型资源
    public LabResource getLabResourceById(int id);    //空就返回NULL
    public boolean addLabResource(LabResource labResource);
    public boolean updateLabResourceForId(LabResource labResource);  //在对应位置上修改id
    public boolean deleteLabResourceById(int id);  //只在形式上删除

    public boolean isLabResourceIdExist(int id);

    // ==================== 实验详情管理 ====================
    public List<LabInfo> getAllLabInfos();
    public LabInfo getLabInfoByLabId(int labId);  //一个实验只有一个详情
    public LabInfo getLabInfoById(int id);    //空就返回NULL
    public boolean addLabInfo(LabInfo labInfo);
    public boolean updateLabInfoForId(LabInfo labInfo);  //在对应位置上修改id
    public boolean deleteLabInfoById(int id);  //只在形式上删除

    public boolean isLabInfoIdExist(int id);
    public boolean isLabInfoExistForLab(int labId);

    // ==================== 药材关联查询 ====================
    public List<Integer> getHerbsOnLab(int labId);   //获得实验上的所有药材ID
    public List<Integer> getLabsOnHerb(int herbId);   //获得药材上的所有实验ID
    public boolean isExistHerbOnLab(int labId);   //检测实验是否有药材映射
    public boolean isExistLabOnHerb(int herbId);   //检测药材是否有实验映射

    // ==================== 分类查询 ====================
    public List<Course> getCoursesByType(String courseType);  //按课程类型查询
    public List<Course> getCoursesByObject(String courseObject);  //按目标级别查询

    // ==================== 搜索功能 ====================
    public List<Course> searchCourses(String keyword, String courseType, String courseObject);  //课程关键词搜索
    public List<Lab> searchLabsByKeyword(String keyword);  //实验关键词搜索

    // ==================== 资源管理 ====================
    public String getResourcePreviewUrl(int resourceId, String resourceType);  //获取资源预览URL
    public String getResourceDownloadUrl(int resourceId, String resourceType);  //获取资源下载URL
    public boolean isResourceExist(int resourceId, String resourceType);  //检查资源是否存在

    // ==================== DTO转换 ====================
    public Course transferDTOToCourse(CourseDTO courseDTO);
    public CourseDTO transferCourseToDTO(Course course);
}