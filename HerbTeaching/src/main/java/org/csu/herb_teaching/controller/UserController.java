package org.csu.herb_teaching.controller;

import lombok.RequiredArgsConstructor;
import org.csu.herb_teaching.VO.PageVO;
import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final CourseService courseService;

    // GET /users/{userId}/collections - Get user's collected courses
    @GetMapping("/{userId}/collections")
    public ResponseEntity<?> getCollectedCourses(
            @PathVariable int userId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageVO<Course> courses = courseService.getCollectedCoursesByUserId(userId, pageNum, pageSize);
        Map<String, Object> result = Map.of(
                "code", 0,
                "message", "User's collected courses retrieved successfully.",
                "courses", courses
        );
        return ResponseEntity.ok(result);
    }
} 