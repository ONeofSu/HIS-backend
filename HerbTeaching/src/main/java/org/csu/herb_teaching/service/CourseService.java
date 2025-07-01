package org.csu.herb_teaching.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.csu.herb_teaching.DTO.CourseDTO;
import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.entity.CourseRating;
import org.csu.herb_teaching.entity.UserCourseCollection;
import org.csu.herb_teaching.VO.CourseDetailVO;
import org.csu.herb_teaching.VO.PageVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseService extends IService<Course> {

    // --- Course Management ---
    PageVO<Course> getCourseList(int pageNum, int pageSize, String keyword, int courseType, int courseObject);
    CourseDetailVO getCourseDetail(int courseId);
    Course createCourse(CourseDTO courseDTO);
    Course updateCourse(CourseDTO courseDTO);
    boolean deleteCourse(int courseId);

    // --- Course Interaction ---
    CourseRating rateCourse(int courseId, int userId, int rating);
    UserCourseCollection collectCourse(int courseId, int userId);
    boolean removeCollection(int courseId, int userId);

    // --- User-specific Query ---
    PageVO<Course> getCollectedCoursesByUserId(int userId, int pageNum, int pageSize);

    // --- Course Herb Management ---
    boolean addHerbToCourse(int courseId, int herbId);
    boolean removeHerbFromCourse(int courseId, int herbId);
    boolean updateCourseHerbs(int courseId, List<Integer> herbIds);
    List<Integer> getCourseHerbIds(int courseId);

}