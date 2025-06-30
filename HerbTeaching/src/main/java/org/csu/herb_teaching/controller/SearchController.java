package org.csu.herb_teaching.controller;

import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.entity.Lab;
import org.csu.herb_teaching.entity.LabResource;
import org.csu.herb_teaching.service.CourseService;
import org.csu.herb_teaching.utils.DataConvertUtil;
import org.csu.herb_teaching.utils.ResponseUtil;
import org.csu.herb_teaching.VO.CourseVO;
import org.csu.herb_teaching.VO.LabVO;
import org.csu.herb_teaching.VO.HerbVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private CourseService courseService;

    // 1.1搜索实验
    @GetMapping("/labs")
    public ResponseEntity<Map<String, Object>> searchLabs(@RequestParam String keyword) {
        try {
            long startTime = System.currentTimeMillis();
            List<Lab> labs = courseService.searchLabsByKeyword(keyword);
            long searchTime = System.currentTimeMillis() - startTime;

            List<LabVO> labVOs = labs.stream()
                .map(lab -> {
                    Course course = courseService.getCourseById(lab.getCourseId());
                    List<LabResource> resources = courseService.getLabResourcesByLabId(lab.getId());
                    HerbVO herbInfo = DataConvertUtil.createHerbVO(1);
                    return DataConvertUtil.labToVO(lab, course, resources.size(), herbInfo);
                })
                .collect(Collectors.toList());

            Map<String, Object> searchInfo = new HashMap<>();
            searchInfo.put("keyword", keyword);
            searchInfo.put("resultCount", labs.size());
            searchInfo.put("searchTime", String.format("%.2fs", searchTime / 1000.0));

            Map<String, Object> data = new HashMap<>();
            data.put("searchInfo", searchInfo);
            data.put("labs", labVOs);

            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error("搜索实验失败");
        }
    }

    // 1.2搜索课程
    @GetMapping("/courses")
    public ResponseEntity<Map<String, Object>> searchCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String courseType,
            @RequestParam(required = false) String courseObject) {
        try {
            long startTime = System.currentTimeMillis();
            List<Course> courses = courseService.searchCourses(keyword, courseType, courseObject);
            long searchTime = System.currentTimeMillis() - startTime;

            List<CourseVO> courseVOs = courses.stream()
                .map(course -> {
                    List<Lab> labs = courseService.getLabsByCourseId(course.getId());
                    return DataConvertUtil.courseToVO(course, labs.size());
                })
                .collect(Collectors.toList());

            Map<String, Object> searchInfo = new HashMap<>();
            searchInfo.put("keyword", keyword);
            
            Map<String, Object> filters = new HashMap<>();
            if (courseType != null && !courseType.isEmpty()) {
                filters.put("courseType", courseType);
                filters.put("courseTypeName", DataConvertUtil.getCourseTypeName(Integer.parseInt(courseType)));
            }
            if (courseObject != null && !courseObject.isEmpty()) {
                filters.put("courseObject", courseObject);
                filters.put("courseObjectName", DataConvertUtil.getCourseObjectName(Integer.parseInt(courseObject)));
            }
            searchInfo.put("filters", filters);
            searchInfo.put("resultCount", courses.size());
            searchInfo.put("searchTime", String.format("%.2fs", searchTime / 1000.0));

            Map<String, Object> data = new HashMap<>();
            data.put("searchInfo", searchInfo);
            data.put("courses", courseVOs);

            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error("搜索课程失败");
        }
    }
} 