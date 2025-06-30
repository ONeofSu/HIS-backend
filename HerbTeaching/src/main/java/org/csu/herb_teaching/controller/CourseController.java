package org.csu.herb_teaching.controller;

import org.csu.herb_teaching.DTO.CourseDTO;
import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.entity.Lab;
import org.csu.herb_teaching.entity.LabResource;
import org.csu.herb_teaching.service.CourseService;
import org.csu.herb_teaching.utils.DataConvertUtil;
import org.csu.herb_teaching.utils.ResponseUtil;
import org.csu.herb_teaching.VO.CourseVO;
import org.csu.herb_teaching.VO.LabVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // 1.1获取所有课程信息
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            
            List<CourseVO> courseVOs = courses.stream()
                .map(course -> {
                    // 获取每个课程的实验数量
                    List<Lab> labs = courseService.getLabsByCourseId(course.getId());
                    return DataConvertUtil.courseToVO(course, labs.size());
                })
                .collect(Collectors.toList());
            
            Map<String, Object> data = new HashMap<>();
            data.put("courses", courseVOs);
            
            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error("获取课程列表失败");
        }
    }

    // 1.2根据课程名获取课程（使用查询参数）
    @GetMapping("/coursename")
    public ResponseEntity<Map<String, Object>> getCourseByName(@RequestParam String name) {
        try {
            // 处理URL编码问题
            String decodedName = java.net.URLDecoder.decode(name, "UTF-8");
            
            Course course = courseService.getCourseByName(decodedName);
            if (course == null) {
                return ResponseUtil.error("课程不存在: " + decodedName);
            }
            
            // 获取实验数量
            List<Lab> labs = courseService.getLabsByCourseId(course.getId());
            CourseVO courseVO = DataConvertUtil.courseToVO(course, labs.size());
            
            // 获取最近的实验（取前3个）
            List<LabVO> recentLabs = labs.stream()
                .limit(3)
                .map(lab -> {
                    List<LabResource> resources = courseService.getLabResourcesByLabId(lab.getId());
                    return DataConvertUtil.labToVO(lab, course, resources.size(), null);
                })
                .collect(Collectors.toList());
            courseVO.setRecentLabs(recentLabs);
            
            Map<String, Object> data = new HashMap<>();
            data.put("course", courseVO);
            
            return ResponseUtil.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error("查询课程失败: " + e.getMessage());
        }
    }

    // 1.3根据课程名获取课程（备用方法，支持路径参数）
    @GetMapping("/search/{courseName}")
    public ResponseEntity<Map<String, Object>> getCourseByNamePath(@PathVariable String courseName) {
        try {
            // 处理URL编码问题
            String decodedName = java.net.URLDecoder.decode(courseName, "UTF-8");
            
            Course course = courseService.getCourseByName(decodedName);
            if (course == null) {
                return ResponseUtil.error("课程不存在: " + decodedName);
            }
            
            // 获取实验数量
            List<Lab> labs = courseService.getLabsByCourseId(course.getId());
            CourseVO courseVO = DataConvertUtil.courseToVO(course, labs.size());
            
            // 获取最近的实验（取前3个）
            List<LabVO> recentLabs = labs.stream()
                .limit(3)
                .map(lab -> {
                    List<LabResource> resources = courseService.getLabResourcesByLabId(lab.getId());
                    return DataConvertUtil.labToVO(lab, course, resources.size(), null);
                })
                .collect(Collectors.toList());
            courseVO.setRecentLabs(recentLabs);
            
            Map<String, Object> data = new HashMap<>();
            data.put("course", courseVO);
            
            return ResponseUtil.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error("查询课程失败: " + e.getMessage());
        }
    }

    // 2.1新增课程信息
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addCourse(@RequestBody CourseDTO courseDTO) {
        try {
            // 检查课程名是否已存在
            if (courseService.isCourseNameExist(courseDTO.getCourseName())) {
                return ResponseUtil.error("课程名已存在");
            }

            // 转换DTO为实体
            Course course = courseService.transferDTOToCourse(courseDTO);
            
            // 保存课程
            if (courseService.addCourse(course)) {
                CourseVO courseVO = DataConvertUtil.courseToVO(course, 0);
                
                Map<String, Object> data = new HashMap<>();
                data.put("course", courseVO);
                
                return ResponseUtil.success(data);
            } else {
                return ResponseUtil.error("添加课程失败");
            }
        } catch (Exception e) {
            return ResponseUtil.error("添加课程失败");
        }
    }

    // 2.2删除课程信息
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Map<String, Object>> deleteCourse(@PathVariable int courseId) {
        try {
            if (!courseService.isCourseIdExist(courseId)) {
                return ResponseUtil.error("该课程不存在");
            }

            Course course = courseService.getCourseById(courseId);
            if (courseService.deleteCourseById(courseId)) {
                Map<String, Object> deletedCourse = new HashMap<>();
                deletedCourse.put("courseId", courseId);
                deletedCourse.put("courseName", course.getName());
                
                Map<String, Object> data = new HashMap<>();
                data.put("deletedCourse", deletedCourse);
                
                return ResponseUtil.success(data);
            } else {
                return ResponseUtil.error("删除课程失败");
            }
        } catch (Exception e) {
            return ResponseUtil.error("删除课程失败");
        }
    }
} 