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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final LabService labService;
    private final ResourceService resourceService;

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
    public ResponseUtil<Course> createCourse(@RequestBody CourseDTO courseDTO) {
        Course newCourse = courseService.createCourse(courseDTO);
        if (newCourse != null) {
            return new ResponseUtil<>(0, "Course created successfully.", newCourse);
        }
        return new ResponseUtil<>(-1, "课程名已存在", null);
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
    public ResponseUtil<CourseRating> rateCourse(
            @PathVariable int courseId,
            @RequestBody Map<String, Integer> payload,
            @RequestHeader("userId") int userId) {
        try {
            int rating = payload.get("ratingValue");
            CourseRating courseRating = courseService.rateCourse(courseId, userId, rating);
            if (courseRating == null) {
                return new ResponseUtil<>(-1, "Course not found.", null);
            }
            return new ResponseUtil<>(0, "Rating submitted successfully.", courseRating);
        } catch (Exception e) {
            return new ResponseUtil<>(-1, e.getMessage(), null);
        }
    }

    // POST /courses/{courseId}/collections - Collect a course
    @PostMapping("/{courseId}/collections")
    public ResponseUtil<UserCourseCollection> collectCourse(@PathVariable int courseId, @RequestHeader("userId") int userId) {
        UserCourseCollection collection = courseService.collectCourse(courseId, userId);
        if (collection != null) {
            return new ResponseUtil<>(0, "Course collected successfully.", collection);
        }
        return new ResponseUtil<>(-1, "Course already in collection or does not exist.", null);
    }

    // DELETE /courses/{courseId}/collections - Uncollect a course
    @DeleteMapping("/{courseId}/collections")
    public ResponseUtil<Object> removeCollection(@PathVariable int courseId, @RequestHeader("userId") int userId) {
        boolean success = courseService.removeCollection(courseId, userId);
        if (success) {
            return new ResponseUtil<>(0, "Course uncollected successfully.", null);
        }
        return new ResponseUtil<>(-1, "Course not in collection or does not exist.", null);
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
    public ResponseUtil<Object> addHerbToCourse(@PathVariable int courseId, @PathVariable int herbId) {
        boolean success = courseService.addHerbToCourse(courseId, herbId);
        if (success) {
            return new ResponseUtil<>(0, "Herb added to course successfully.", null);
        }
        return new ResponseUtil<>(-1, "Failed to add herb to course. Course not found, herb not found, or herb already exists in course.", null);
    }

    // DELETE /courses/{courseId}/herbs/{herbId} - Remove a herb from a course (Teacher+)
    @DeleteMapping("/{courseId}/herbs/{herbId}")
    public ResponseUtil<Object> removeHerbFromCourse(@PathVariable int courseId, @PathVariable int herbId) {
        boolean success = courseService.removeHerbFromCourse(courseId, herbId);
        if (success) {
            return new ResponseUtil<>(0, "Herb removed from course successfully.", null);
        }
        return new ResponseUtil<>(-1, "Failed to remove herb from course. Course not found or herb not in course.", null);
    }

    // PUT /courses/{courseId}/herbs - Update all herbs for a course (Teacher+)
    @PutMapping("/{courseId}/herbs")
    public ResponseUtil<Object> updateCourseHerbs(@PathVariable int courseId, @RequestBody CourseHerbDTO courseHerbDTO) {
        courseHerbDTO.setCourseId(courseId); // 确保courseId一致
        boolean success = courseService.updateCourseHerbs(courseId, courseHerbDTO.getHerbIds());
        if (success) {
            return new ResponseUtil<>(0, "Course herbs updated successfully.", null);
        }
        return new ResponseUtil<>(-1, "Failed to update course herbs. Course not found or some herbs not found.", null);
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
} 