package org.csu.herb_teaching.controller;

import lombok.RequiredArgsConstructor;
import org.csu.herb_teaching.DTO.CourseDTO;
import org.csu.herb_teaching.DTO.CourseResourceDTO;
import org.csu.herb_teaching.DTO.LabDTO;
import org.csu.herb_teaching.DTO.CourseHerbDTO;
import org.csu.herb_teaching.VO.CourseDetailVO;
import org.csu.herb_teaching.VO.PageVO;
import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.entity.CourseRating;
import org.csu.herb_teaching.entity.CourseResource;
import org.csu.herb_teaching.entity.Lab;
import org.csu.herb_teaching.entity.UserCourseCollection;
import org.csu.herb_teaching.service.CourseService;
import org.csu.herb_teaching.service.LabService;
import org.csu.herb_teaching.service.ResourceService;
import org.csu.herb_teaching.utils.ResponseUtil;
import org.csu.herb_teaching.feign.UserFeignClient;
import org.csu.herb_teaching.feign.HerbInfoFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final LabService labService;
    private final ResourceService resourceService;
    private final UserFeignClient userFeignClient;
    private final HerbInfoFeignClient herbInfoFeignClient;

    // GET /courses/{courseId} - Get course details
    @GetMapping("/{courseId}")
    public ResponseUtil<CourseDetailVO> getCourseDetail(@PathVariable int courseId) {
        CourseDetailVO courseDetails = courseService.getCourseDetail(courseId);
        if (courseDetails == null) {
            return new ResponseUtil<>(-1, "Course not found.", null);
        }
        return new ResponseUtil<>(0, "Course details retrieved successfully.", courseDetails);
    }

    // GET /courses - Get all courses with optional filters and pagination
    @GetMapping
    public ResponseUtil<PageVO<Course>> getCourseList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "0") int courseType,
            @RequestParam(required = false, defaultValue = "0") int courseObject,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageVO<Course> courses = courseService.getCourseList(pageNum, pageSize, keyword, courseType, courseObject);
        return new ResponseUtil<>(0, "Courses retrieved successfully.", courses);
    }

    // POST /courses - Create a new course (Teacher+)
    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CourseDTO courseDTO) {
        // 课程名校验
        if (courseService.getCourseList(1, 1, courseDTO.getCourseName(), 0, 0).getList().size() > 0) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "课程名已存在");
            return ResponseEntity.ok(result);
        }
        // 教师校验
        Boolean isTeacher;
        try {
            isTeacher = userFeignClient.isUserRealTeacher(courseDTO.getTeacherId());
        } catch (Exception e) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "teacherId不是有效教师");
            return ResponseEntity.ok(result);
        }
        if (isTeacher == null || !isTeacher) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "teacherId不是有效教师");
            return ResponseEntity.ok(result);
        }
        Course newCourse = courseService.createCourse(courseDTO);
        if (newCourse != null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", 0);
            result.put("course", newCourse);
            result.put("teacherId", newCourse.getTeacherId());
            result.put("courseName", newCourse.getCourseName());
            return ResponseEntity.ok(result);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", -1);
        result.put("message", "未知错误");
        return ResponseEntity.ok(result);
    }

    // PUT /courses/{courseId} - Update a course (Teacher+)
    @PutMapping("/{courseId}")
    public ResponseUtil<Course> updateCourse(@PathVariable int courseId, @RequestBody CourseDTO courseDTO) {
        courseDTO.setCourseId(courseId); // Manually set the ID from path variable
        Course updatedCourse = courseService.updateCourse(courseDTO);
        if (updatedCourse != null) {
            return new ResponseUtil<>(0, "Course updated successfully.", updatedCourse);
        }
        return new ResponseUtil<>(-1, "Course not found.", null);
    }

    // DELETE /courses/{courseId} - Delete a course (Teacher+)
    @DeleteMapping("/{courseId}")
    public ResponseUtil<Object> deleteCourse(@PathVariable int courseId) {
        boolean success = courseService.deleteCourse(courseId);
        if (success) {
            return new ResponseUtil<>(0, "Course deleted successfully.", null);
        }
        return new ResponseUtil<>(-1, "Course not found.", null);
    }

    // POST /courses/{courseId}/ratings - Rate a course
    @PostMapping("/{courseId}/ratings")
    public ResponseEntity<?> rateCourse(
            @PathVariable int courseId,
            @RequestBody Map<String, Integer> payload,
            @RequestHeader("userId") int userId) {
        // 校验课程是否存在
        if (courseService.getCourseDetail(courseId) == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "课程不存在");
            return ResponseEntity.ok(result);
        }
        // 校验用户是否存在
        Boolean userExist;
        try {
            userExist = userFeignClient.isUserExist(userId);
        } catch (Exception e) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
        if (userExist == null || !userExist) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
        // 校验评分范围
        int rating = payload.get("ratingValue");
        if (rating < 0 || rating > 5) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "评分必须在0到5之间");
            return ResponseEntity.ok(result);
        }
        CourseRating courseRating = courseService.rateCourse(courseId, userId, rating);
        if (courseRating == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "未知错误");
            return ResponseEntity.ok(result);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 0);
        result.put("courseRating", courseRating);
        result.put("courseId", courseId);
        result.put("userId", userId);
        result.put("rating", rating);
        return ResponseEntity.ok(result);
    }

    // POST /courses/{courseId}/collections - Collect a course
    @PostMapping("/{courseId}/collections")
    public ResponseEntity<?> collectCourse(@PathVariable int courseId, @RequestHeader("userId") int userId) {
        // 校验课程是否存在
        if (courseService.getCourseDetail(courseId) == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "课程不存在");
            return ResponseEntity.ok(result);
        }
        // 校验用户是否存在
        Boolean userExist;
        try {
            userExist = userFeignClient.isUserExist(userId);
        } catch (Exception e) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
        if (userExist == null || !userExist) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
        // 只调用一次collectCourse
        UserCourseCollection collection = courseService.collectCourse(courseId, userId);
        if (collection == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "已收藏，无需重复收藏");
            return ResponseEntity.ok(result);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 0);
        result.put("collection", collection);
        result.put("courseId", courseId);
        result.put("userId", userId);
        return ResponseEntity.ok(result);
    }

    // DELETE /courses/{courseId}/collections - Uncollect a course
    @DeleteMapping("/{courseId}/collections")
    public ResponseEntity<?> removeCollection(@PathVariable int courseId, @RequestHeader("userId") int userId) {
        // 校验课程是否存在
        if (courseService.getCourseDetail(courseId) == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "课程不存在");
            return ResponseEntity.ok(result);
        }
        // 校验用户是否存在
        Boolean userExist;
        try {
            userExist = userFeignClient.isUserExist(userId);
        } catch (Exception e) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
        if (userExist == null || !userExist) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
        // 校验是否已收藏
        boolean success = courseService.removeCollection(courseId, userId);
        if (!success) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "未收藏，无需取消");
            return ResponseEntity.ok(result);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 0);
        result.put("courseId", courseId);
        result.put("userId", userId);
        return ResponseEntity.ok(result);
    }

    // POST /courses/{courseId}/labs - Create a new lab for a course (Teacher+)
    @PostMapping("/{courseId}/labs")
    public ResponseUtil<Lab> createLab(@PathVariable int courseId, @RequestBody LabDTO labDTO) {
        Lab newLab = labService.createLab(courseId, labDTO);
        if (newLab != null) {
            return new ResponseUtil<>(0, "Lab created successfully.", newLab);
        }
        return new ResponseUtil<>(-1, "Failed to create lab.", null);
    }

    // GET /courses/{courseId}/labs - Get all labs for a course
    @GetMapping("/{courseId}/labs")
    public ResponseUtil<List<Lab>> getLabsByCourse(@PathVariable int courseId) {
        if (courseService.getCourseDetail(courseId) == null) {
            return new ResponseUtil<>(-1, "Course not found.", null);
        }
        List<Lab> labs = labService.getLabsByCourseId(courseId);
        return new ResponseUtil<>(0, "Success", labs);
    }

    // POST /courses/{courseId}/resources - Add a resource to a course (Teacher+)
    @PostMapping("/{courseId}/resources")
    public ResponseUtil<CourseResource> createResource(@PathVariable int courseId, @RequestBody CourseResourceDTO resourceDTO) {
        CourseResource newResource = resourceService.createResource(courseId, resourceDTO);
        if (newResource != null) {
            return new ResponseUtil<>(0, "Resource created successfully.", newResource);
        }
        return new ResponseUtil<>(-1, "Failed to add resource.", null);
    }

    // GET /courses/{courseId}/resources - Get all resources for a course
    @GetMapping("/{courseId}/resources")
    public ResponseUtil<List<CourseResource>> getResourcesByCourse(@PathVariable int courseId) {
        if (courseService.getCourseDetail(courseId) == null) {
            return new ResponseUtil<>(-1, "Course not found.", null);
        }
        List<CourseResource> resources = resourceService.getResourcesByCourseId(courseId);
        return new ResponseUtil<>(0, "Success", resources);
    }

    // --- Course Herb Management APIs ---

    // POST /courses/{courseId}/herbs/{herbId} - Add a herb to a course (Teacher+)
    @PostMapping("/{courseId}/herbs/{herbId}")
    public ResponseEntity<?> addHerbToCourse(@PathVariable int courseId, @PathVariable int herbId) {
        // 校验课程是否存在
        if (courseService.getCourseDetail(courseId) == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "课程不存在");
            return ResponseEntity.ok(result);
        }
        // 校验中草药是否存在
        Map<String, Object> herbInfo = herbInfoFeignClient.getHerbInfoById(herbId);
        if (herbInfo == null || herbInfo.get("herbId") == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "中草药不存在");
            return ResponseEntity.ok(result);
        }
        // 校验是否已存在关联
        if (courseService.isHerbLinkedToCourse(courseId, herbId)) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "该中草药已在课程中，无需重复添加");
            return ResponseEntity.ok(result);
        }
        boolean success = courseService.addHerbToCourse(courseId, herbId);
        if (success) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", 0);
            result.put("courseId", courseId);
            result.put("herbId", herbId);
            return ResponseEntity.ok(result);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", -1);
        result.put("message", "未知错误");
        return ResponseEntity.ok(result);
    }

    // DELETE /courses/{courseId}/herbs/{herbId} - Remove a herb from a course (Teacher+)
    @DeleteMapping("/{courseId}/herbs/{herbId}")
    public ResponseEntity<?> removeHerbFromCourse(@PathVariable int courseId, @PathVariable int herbId) {
        // 校验课程是否存在
        if (courseService.getCourseDetail(courseId) == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "课程不存在");
            return ResponseEntity.ok(result);
        }
        // 校验中草药是否存在
        Map<String, Object> herbInfo = herbInfoFeignClient.getHerbInfoById(herbId);
        if (herbInfo == null || herbInfo.get("herbId") == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "中草药不存在");
            return ResponseEntity.ok(result);
        }
        // 校验是否已存在关联
        if (!courseService.isHerbLinkedToCourse(courseId, herbId)) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "该中草药未关联到课程，无需删除");
            return ResponseEntity.ok(result);
        }
        boolean success = courseService.removeHerbFromCourse(courseId, herbId);
        if (success) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", 0);
            result.put("courseId", courseId);
            result.put("herbId", herbId);
            return ResponseEntity.ok(result);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", -1);
        result.put("message", "未知错误");
        return ResponseEntity.ok(result);
    }

    // PUT /courses/{courseId}/herbs - Update all herbs for a course (Teacher+)
    @PutMapping("/{courseId}/herbs")
    public ResponseEntity<?> updateCourseHerbs(@PathVariable int courseId, @RequestBody CourseHerbDTO courseHerbDTO) {
        courseHerbDTO.setCourseId(courseId); // 确保courseId一致
        // 校验课程是否存在
        if (courseService.getCourseDetail(courseId) == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "课程不存在");
            return ResponseEntity.ok(result);
        }
        // 校验所有中草药是否存在
        for (Integer herbId : courseHerbDTO.getHerbIds()) {
            Map<String, Object> herbInfo = herbInfoFeignClient.getHerbInfoById(herbId);
            if (herbInfo == null || herbInfo.get("herbId") == null) {
                Map<String, Object> result = new LinkedHashMap<>();
                result.put("code", -1);
                result.put("message", "中草药不存在，herbId=" + herbId);
                return ResponseEntity.ok(result);
            }
        }
        boolean success = courseService.updateCourseHerbs(courseId, courseHerbDTO.getHerbIds());
        if (success) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", 0);
            result.put("courseId", courseId);
            result.put("herbIds", courseHerbDTO.getHerbIds());
            return ResponseEntity.ok(result);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", -1);
        result.put("message", "未知错误");
        return ResponseEntity.ok(result);
    }

    // GET /courses/{courseId}/herbs - Get all herb IDs for a course
    @GetMapping("/{courseId}/herbs")
    public ResponseUtil<List<Integer>> getCourseHerbIds(@PathVariable int courseId) {
        List<Integer> herbIds = courseService.getCourseHerbIds(courseId);
        if (herbIds.isEmpty() && courseService.getCourseDetail(courseId) == null) {
            return new ResponseUtil<>(-1, "Course not found.", null);
        }
        return new ResponseUtil<>(0, "Course herb IDs retrieved successfully.", herbIds);
    }

    @GetMapping("/collections")
    public ResponseEntity<?> getCollectedCoursesByUserId(@RequestHeader("userId") int userId,
                                                        @RequestParam(defaultValue = "1") int pageNum,
                                                        @RequestParam(defaultValue = "10") int pageSize) {
        // 校验用户是否存在
        Boolean userExist;
        try {
            userExist = userFeignClient.isUserExist(userId);
        } catch (Exception e) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
        if (userExist == null || !userExist) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "用户不存在");
            return ResponseEntity.ok(result);
        }
        PageVO<Course> pageVO = courseService.getCollectedCoursesByUserId(userId, pageNum, pageSize);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 0);
        result.put("data", pageVO);
        result.put("userId", userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{courseId}/exist")
    public boolean isCourseExist(@PathVariable int courseId) {
        return courseService.getById(courseId) != null;
    }
} 