package org.csu.herb_teaching.controller;

import lombok.RequiredArgsConstructor;
import org.csu.herb_teaching.VO.PageVO;
import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.service.CourseService;
import org.csu.herb_teaching.utils.ResponseUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final CourseService courseService;

    // GET /users/{userId}/collections - Get user's collected courses
    @GetMapping("/{userId}/collections")
    public ResponseUtil<PageVO<Course>> getCollectedCourses(
            @PathVariable int userId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageVO<Course> courses = courseService.getCollectedCoursesByUserId(userId, pageNum, pageSize);
        return new ResponseUtil<>(0, "User's collected courses retrieved successfully.", courses);
    }
} 