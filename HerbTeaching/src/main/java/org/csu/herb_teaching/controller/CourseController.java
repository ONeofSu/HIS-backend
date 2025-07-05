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
    public ResponseEntity<?> getCourseDetail(@PathVariable int courseId) {
        CourseDetailVO courseDetails = courseService.getCourseDetail(courseId);
        if (courseDetails == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "Course not found.");
            return ResponseEntity.ok(result);
        }
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 0);
        response.put("message", "Course details retrieved successfully.");
        response.put("data", courseDetails);
        return ResponseEntity.ok(response);
    }

    // GET /courses - Get all courses with optional filters and pagination
    @GetMapping
    public ResponseEntity<?> getCourseList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "0") int courseType,
            @RequestParam(required = false, defaultValue = "0") int courseObject,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageVO<Course> courses = courseService.getCourseList(pageNum, pageSize, keyword, courseType, courseObject);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 0);
        response.put("message", "Courses retrieved successfully.");
        response.put("data", courses);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<?> updateCourse(@PathVariable int courseId, @RequestBody CourseDTO courseDTO) {
        courseDTO.setCourseId(courseId); // Manually set the ID from path variable
        Course updatedCourse = courseService.updateCourse(courseDTO);
        if (updatedCourse != null) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("code", 0);
            response.put("message", "Course updated successfully.");
            response.put("data", updatedCourse);
            return ResponseEntity.ok(response);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", -1);
        result.put("message", "Course not found.");
        return ResponseEntity.ok(result);
    }

    // DELETE /courses/{courseId} - Delete a course (Teacher+)
    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable int courseId) {
        boolean success = courseService.deleteCourse(courseId);
        if (success) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", 0);
            result.put("message", "Course deleted successfully.");
            return ResponseEntity.ok(result);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", -1);
        result.put("message", "Course not found.");
        return ResponseEntity.ok(result);
    }

    // POST /courses/{courseId}/ratings - Rate a course
    @PostMapping("/{courseId}/ratings")
    public ResponseEntity<?> rateCourse(
            @PathVariable int courseId,
            @RequestBody Map<String, Integer> payload,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
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
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 0);
        response.put("message", "Course rating recorded successfully.");
        response.put("courseRating", courseRating);
        response.put("courseId", courseId);
        response.put("userId", userId);
        response.put("rating", rating);
        return ResponseEntity.ok(response);
    }

    // POST /courses/{courseId}/collections - Collect a course
    @PostMapping("/{courseId}/collections")
    public ResponseEntity<?> collectCourse(@PathVariable int courseId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
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
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 0);
        response.put("message", "Course collection recorded successfully.");
        response.put("collection", collection);
        response.put("courseId", courseId);
        response.put("userId", userId);
        return ResponseEntity.ok(response);
    }

    // DELETE /courses/{courseId}/collections - Uncollect a course
    @DeleteMapping("/{courseId}/collections")
    public ResponseEntity<?> removeCollection(@PathVariable int courseId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
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
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 0);
        response.put("message", "Course collection removed successfully.");
        response.put("courseId", courseId);
        response.put("userId", userId);
        return ResponseEntity.ok(response);
    }

    // POST /courses/{courseId}/labs - Create a new lab for a course (Teacher+)
    @PostMapping("/{courseId}/labs")
    public ResponseEntity<?> createLab(@PathVariable int courseId, @RequestBody LabDTO labDTO) {
        Lab newLab = labService.createLab(courseId, labDTO);
        if (newLab != null) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("code", 0);
            response.put("message", "Lab created successfully.");
            response.put("data", newLab);
            return ResponseEntity.ok(response);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", -1);
        result.put("message", "Failed to create lab.");
        return ResponseEntity.ok(result);
    }

    // GET /courses/{courseId}/labs - Get all labs for a course
    @GetMapping("/{courseId}/labs")
    public ResponseEntity<?> getLabsByCourse(@PathVariable int courseId) {
        if (courseService.getCourseDetail(courseId) == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "Course not found.");
            return ResponseEntity.ok(result);
        }
        List<Lab> labs = labService.getLabsByCourseId(courseId);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 0);
        response.put("message", "Success");
        response.put("data", labs);
        return ResponseEntity.ok(response);
    }

    // POST /courses/{courseId}/resources - Add a resource to a course (Teacher+)
    @PostMapping("/{courseId}/resources")
    public ResponseEntity<?> createResource(@PathVariable int courseId, @RequestBody CourseResourceDTO resourceDTO) {
        CourseResource newResource = resourceService.createResource(courseId, resourceDTO);
        if (newResource != null) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("code", 0);
            response.put("message", "Resource created successfully.");
            response.put("data", newResource);
            return ResponseEntity.ok(response);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", -1);
        result.put("message", "Failed to add resource.");
        return ResponseEntity.ok(result);
    }

    // GET /courses/{courseId}/resources - Get all resources for a course
    @GetMapping("/{courseId}/resources")
    public ResponseEntity<?> getResourcesByCourse(@PathVariable int courseId) {
        if (courseService.getCourseDetail(courseId) == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "Course not found.");
            return ResponseEntity.ok(result);
        }
        List<CourseResource> resources = resourceService.getResourcesByCourseId(courseId);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 0);
        response.put("message", "Success");
        response.put("data", resources);
        return ResponseEntity.ok(response);
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
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("code", 0);
            response.put("message", "Herb added to course successfully.");
            response.put("courseId", courseId);
            response.put("herbId", herbId);
            return ResponseEntity.ok(response);
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
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("code", 0);
            response.put("message", "Herb removed from course successfully.");
            response.put("courseId", courseId);
            response.put("herbId", herbId);
            return ResponseEntity.ok(response);
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
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("code", 0);
            response.put("message", "Course herbs updated successfully.");
            response.put("courseId", courseId);
            response.put("herbIds", courseHerbDTO.getHerbIds());
            return ResponseEntity.ok(response);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", -1);
        result.put("message", "未知错误");
        return ResponseEntity.ok(result);
    }

    // GET /courses/{courseId}/herbs - Get all herb IDs for a course
    @GetMapping("/{courseId}/herbs")
    public ResponseEntity<?> getCourseHerbIds(@PathVariable int courseId) {
        List<Integer> herbIds = courseService.getCourseHerbIds(courseId);
        if (herbIds.isEmpty() && courseService.getCourseDetail(courseId) == null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", -1);
            result.put("message", "Course not found.");
            return ResponseEntity.ok(result);
        }
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 0);
        response.put("message", "Course herb IDs retrieved successfully.");
        response.put("data", herbIds);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/collections")
    public ResponseEntity<?> getCollectedCoursesByUserId(@RequestHeader("Authorization") String authHeader,
                                                        @RequestParam(defaultValue = "1") int pageNum,
                                                        @RequestParam(defaultValue = "10") int pageSize) {
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
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
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 0);
        response.put("message", "Collected courses retrieved successfully.");
        response.put("data", pageVO);
        response.put("userId", userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}/exist")
    public boolean isCourseExist(@PathVariable int courseId) {
        return courseService.getById(courseId) != null;
    }

    // GET /courses/{courseId}/ratings/check - Check if user has rated the course
    @GetMapping("/{courseId}/ratings/check")
    public ResponseEntity<?> checkUserRating(@PathVariable int courseId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
        
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
        
        boolean hasRated = courseService.hasUserRatedCourse(courseId, userId);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 0);
        response.put("message", "User rating check completed successfully.");
        response.put("courseId", courseId);
        response.put("userId", userId);
        response.put("hasRated", hasRated);
        return ResponseEntity.ok(response);
    }

    // GET /courses/{courseId}/ratings/user - Get user's rating for the course
    @GetMapping("/{courseId}/ratings/user")
    public ResponseEntity<?> getUserRating(@PathVariable int courseId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
        
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
        
        CourseRating userRating = courseService.getUserRating(courseId, userId);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 0);
        response.put("message", "User rating retrieved successfully.");
        response.put("courseId", courseId);
        response.put("userId", userId);
        response.put("hasRated", userRating != null);
        response.put("rating", userRating);
        return ResponseEntity.ok(response);
    }

    // GET /courses/{courseId}/collections/user - Check if user has collected the course
    @GetMapping("/{courseId}/collections/user")
    public ResponseEntity<?> checkUserCollection(@PathVariable int courseId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
        
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
        
        boolean hasCollected = courseService.hasUserCollectedCourse(courseId, userId);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", 0);
        response.put("message", "User collection check completed successfully.");
        response.put("courseId", courseId);
        response.put("userId", userId);
        response.put("hasCollected", hasCollected);
        return ResponseEntity.ok(response);
    }
} 