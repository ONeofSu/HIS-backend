package org.csu.herb_teaching.utils;

import org.csu.herb_teaching.entity.*;
import org.csu.herb_teaching.VO.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

public class DataConvertUtil {
    
    /**
     * 获取实验类型名称
     */
    public static String getLabTypeName(int labType) {
        switch (labType) {
            case 0: return "选修";
            case 1: return "必修";
            default: return "未知";
        }
    }
    
    /**
     * 获取实验对象名称
     */
    public static String getLabObjectName(int labObject) {
        switch (labObject) {
            case 0: return "本科生";
            case 1: return "研究生";
            case 2: return "博士生";
            default: return "未知";
        }
    }
    
    /**
     * 获取课程类型名称
     */
    public static String getCourseTypeName(int courseType) {
        switch (courseType) {
            case 0: return "选修";
            case 1: return "必修";
            default: return "未知";
        }
    }
    
    /**
     * 获取课程对象名称
     */
    public static String getCourseObjectName(int courseObject) {
        switch (courseObject) {
            case 0: return "本科生";
            case 1: return "研究生";
            case 2: return "博士生";
            default: return "未知";
        }
    }
    
    /**
     * 获取资源类型名称
     */
    public static String getResourceTypeName(int resourceType) {
        switch (resourceType) {
            case 0: return "文本";
            case 1: return "图片";
            case 2: return "视频";
            case 3: return "文档";
            default: return "未知";
        }
    }
    
    /**
     * Course实体转CourseVO
     */
    public static CourseVO courseToVO(Course course, int labCount) {
        if (course == null) return null;
        
        CourseVO vo = new CourseVO();
        vo.setCourseId(course.getId());
        vo.setCourseName(course.getName());
        vo.setCourseStartTime(new java.util.Date(course.getStartTime().getTime()));
        vo.setCourseEndTime(new java.util.Date(course.getEndTime().getTime()));
        vo.setCourseDes(course.getDes());
        vo.setLabCount(labCount);
        vo.setStatus("active");
        vo.setCourseType(course.getCourseType());
        vo.setCourseTypeName(getCourseTypeName(course.getCourseType()));
        vo.setCourseObject(course.getCourseObject());
        vo.setCourseObjectName(getCourseObjectName(course.getCourseObject()));
        return vo;
    }
    
    /**
     * Lab实体转LabVO
     */
    public static LabVO labToVO(Lab lab, Course course, int resourceCount, HerbVO herbInfo) {
        if (lab == null) return null;
        
        LabVO vo = new LabVO();
        vo.setLabId(lab.getId());
        vo.setCourseId(lab.getCourseId());
        vo.setCourseName(course != null ? course.getName() : "");
        vo.setUserId(lab.getUserId());
        vo.setUserName("张老师"); // 这里应该从用户服务获取，暂时写死
        vo.setLabSubmitTime(new java.util.Date(lab.getSubmitTime().getTime()));
        vo.setLabEndTime(new java.util.Date(lab.getEndTime().getTime()));
        vo.setLabDes(lab.getDes());
        vo.setResourceCount(resourceCount);
        vo.setHerbInfo(herbInfo);
        return vo;
    }
    
    /**
     * LabResource实体转LabResourceVO
     */
    public static LabResourceVO resourceToVO(LabResource resource, LabInfoVO labInfo) {
        if (resource == null) return null;
        
        LabResourceVO vo = new LabResourceVO();
        vo.setLabResourceId(resource.getId());
        vo.setLabId(resource.getLabId());
        vo.setLabInfo(labInfo);
        vo.setLabResourceType(resource.getType());
        vo.setLabResourceTypeName(getResourceTypeName(resource.getType()));
        vo.setLabResourceOrder(resource.getResourceOrder());
        vo.setLabResourceTitle(resource.getTitle());
        vo.setLabResourceContent(resource.getContent());
        vo.setLabResourceMetadata(resource.getMetadata());
        vo.setLabResourceTime(new java.util.Date(resource.getTime().getTime()));
        vo.setLabResourceIsvalid(resource.getIsValid());
        vo.setFileSize("1.2MB"); // 这里应该计算实际文件大小
        vo.setPreviewUrl("/preview/" + resource.getId());
        return vo;
    }
    
    /**
     * LabInfo实体转LabInfoVO
     */
    public static LabInfoVO labInfoToVO(LabInfo labInfo, Lab lab, Course course) {
        if (labInfo == null) return null;
        
        LabInfoVO vo = new LabInfoVO();
        vo.setLabInfoId(labInfo.getId());
        vo.setLabId(labInfo.getLabId());
        vo.setLabDes(lab != null ? lab.getDes() : "");
        vo.setCourseName(course != null ? course.getName() : "");
        vo.setHerbId(labInfo.getHerbId());
        vo.setHerbName("白芍"); // 这里应该从药材服务获取，暂时写死
        vo.setLabInfoGoal(labInfo.getGoal());
        vo.setLabInfoSummary(labInfo.getSummary());
        vo.setLabInfoIsvalid(labInfo.getIsValid());
        return vo;
    }
    
    /**
     * 创建HerbVO（使用真实数据）
     */
    public static HerbVO createHerbVO(int herbId) {
        if (herbId <= 0) return null;
        
        HerbVO vo = new HerbVO();
        vo.setHerbId(herbId);
        vo.setHerbName("未知药材"); // 将由 Feign 客户端获取真实数据
        vo.setOrigin("未知产地");
        vo.setImage("default.jpg");
        vo.setRelatedLabCount(0);
        return vo;
    }
    
    /**
     * 根据真实药材数据创建HerbVO
     */
    public static HerbVO createHerbVOFromData(Map<String, Object> herbData) {
        if (herbData == null) return null;
        
        HerbVO vo = new HerbVO();
        vo.setHerbId((Integer) herbData.getOrDefault("id", 0));
        vo.setHerbName((String) herbData.getOrDefault("name", "未知药材"));
        vo.setOrigin((String) herbData.getOrDefault("origin", "未知产地"));
        vo.setImage((String) herbData.getOrDefault("image", "default.jpg"));
        vo.setRelatedLabCount(0); // 这个需要从本地数据库统计
        return vo;
    }
} 