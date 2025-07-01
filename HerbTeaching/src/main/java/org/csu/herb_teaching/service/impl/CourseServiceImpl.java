package org.csu.herb_teaching.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.csu.herb_teaching.DTO.CourseDTO;
import org.csu.herb_teaching.VO.UserVO;
import org.csu.herb_teaching.entity.*;
import org.csu.herb_teaching.feign.HerbInfoFeignClient;
import org.csu.herb_teaching.feign.UserFeignClient;
import org.csu.herb_teaching.mapper.*;
import org.csu.herb_teaching.service.CourseService;
import org.csu.herb_teaching.VO.CourseDetailVO;
import org.csu.herb_teaching.VO.PageVO;
import org.csu.herb_teaching.VO.HerbSimpleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    private final CourseMapper courseMapper;
    private final LabMapper labMapper;
    private final CourseResourceMapper courseResourceMapper;
    private final CourseRatingMapper courseRatingMapper;
    private final UserCourseCollectionMapper userCourseCollectionMapper;
    private final CourseHerbLinkMapper courseHerbLinkMapper;
    private final UserFeignClient userFeignClient;
    private final HerbInfoFeignClient herbInfoFeignClient;

    @Override
    public PageVO<Course> getCourseList(int pageNum, int pageSize, String keyword, int courseType, int courseObject) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("course_name", keyword);
        }
        if (courseType != 0) {
            queryWrapper.eq("course_type", courseType);
        }
        if (courseObject != 0) {
            queryWrapper.eq("course_object", courseObject);
        }

        Page<Course> page = new Page<>(pageNum, pageSize);
        Page<Course> coursePage = courseMapper.selectPage(page, queryWrapper);

        PageVO<Course> pageVO = new PageVO<>();
        pageVO.setTotal(coursePage.getTotal());
        pageVO.setPages(coursePage.getPages());
        pageVO.setList(coursePage.getRecords());
        return pageVO;
    }

    @Override
    public CourseDetailVO getCourseDetail(int courseId) {
        Course course = courseMapper.selectById(courseId);
        if (course == null) return null;

        CourseDetailVO courseDetailVO = new CourseDetailVO();
        BeanUtils.copyProperties(course, courseDetailVO);

        // 获取老师信息
        UserVO teacher = userFeignClient.getUserInfoById(course.getTeacherId());
        courseDetailVO.setTeacherName(teacher != null ? teacher.getUsername() : null);
        courseDetailVO.setTeacherAvatar(teacher != null ? teacher.getAvatarUrl() : null);

        List<Lab> labs = labMapper.selectList(new QueryWrapper<Lab>().eq("course_id", courseId).orderByAsc("lab_order"));
        List<CourseResource> resources = courseResourceMapper.selectList(new QueryWrapper<CourseResource>().eq("course_id", courseId).orderByAsc("course_resource_order"));

        courseDetailVO.setLabs(labs);
        courseDetailVO.setResources(resources);

        // 组装herbs
        List<CourseHerbLink> links = courseHerbLinkMapper.selectList(new QueryWrapper<CourseHerbLink>().eq("course_id", courseId));
        List<HerbSimpleVO> herbs = new ArrayList<>();
        for (CourseHerbLink link : links) {
            Map<String, Object> herbInfo = herbInfoFeignClient.getHerbInfoById(link.getHerbId());
            HerbSimpleVO vo = new HerbSimpleVO();
            vo.setHerbId(link.getHerbId());
            vo.setHerbName(herbInfo != null ? (String) herbInfo.get("herbName") : null);
            vo.setHerbImageUrl(herbInfo != null ? (String) herbInfo.get("herbImageUrl") : null);
            herbs.add(vo);
        }
        courseDetailVO.setHerbs(herbs);

        return courseDetailVO;
    }

    @Override
    @Transactional
    public Course createCourse(CourseDTO courseDTO) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_name", courseDTO.getCourseName());
        if (courseMapper.selectCount(queryWrapper) > 0) {
            return null; // Name exists
        }

        // 新增：校验teacherId是否为教师（只能是教师，不能是管理员）
        Boolean isTeacher = userFeignClient.isUserRealTeacher(courseDTO.getTeacherId());
        if (isTeacher == null || !isTeacher) {
            return null; // teacher_id无效
        }

        Course course = new Course();
        BeanUtils.copyProperties(courseDTO, course);
        course.setCourseAverageRating(BigDecimal.ZERO);
        course.setCourseRatingCount(0);
        
        courseMapper.insert(course);
        return course;
    }

    @Override
    @Transactional
    public Course updateCourse(CourseDTO courseDTO) {
        Course existingCourse = courseMapper.selectById(courseDTO.getCourseId());
        if (existingCourse == null) {
            return null;
        }
        // 只更新非null字段
        if (courseDTO.getCourseName() != null) existingCourse.setCourseName(courseDTO.getCourseName());
        if (courseDTO.getCoverImageUrl() != null) existingCourse.setCoverImageUrl(courseDTO.getCoverImageUrl());
        if (courseDTO.getCourseType() != 0) existingCourse.setCourseType(courseDTO.getCourseType());
        if (courseDTO.getCourseObject() != 0) existingCourse.setCourseObject(courseDTO.getCourseObject());
        if (courseDTO.getTeacherId() != 0) existingCourse.setTeacherId(courseDTO.getTeacherId());
        if (courseDTO.getCourseStartTime() != null) existingCourse.setCourseStartTime(courseDTO.getCourseStartTime());
        if (courseDTO.getCourseEndTime() != null) existingCourse.setCourseEndTime(courseDTO.getCourseEndTime());
        if (courseDTO.getCourseDes() != null) existingCourse.setCourseDes(courseDTO.getCourseDes());
        // 其它字段同理，如有需要可继续补充
        courseMapper.updateById(existingCourse);
        return existingCourse;
    }

    @Override
    @Transactional
    public boolean deleteCourse(int courseId) {
        if (courseMapper.selectById(courseId) == null) {
            return false;
        }
        labMapper.delete(new QueryWrapper<Lab>().eq("course_id", courseId));
        courseResourceMapper.delete(new QueryWrapper<CourseResource>().eq("course_id", courseId));
        courseRatingMapper.delete(new QueryWrapper<CourseRating>().eq("course_id", courseId));
        userCourseCollectionMapper.delete(new QueryWrapper<UserCourseCollection>().eq("course_id", courseId));
        courseHerbLinkMapper.delete(new QueryWrapper<CourseHerbLink>().eq("course_id", courseId));
        courseMapper.deleteById(courseId);
        return true;
    }

    @Override
    @Transactional
    public CourseRating rateCourse(int courseId, int userId, int rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            return null;
        }

        QueryWrapper<CourseRating> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId).eq("user_id", userId);
        CourseRating existingRating = courseRatingMapper.selectOne(queryWrapper);

        if (existingRating != null) {
            existingRating.setRatingValue(rating);
            existingRating.setCreatedAt(LocalDateTime.now());
            courseRatingMapper.updateById(existingRating);
        } else {
            existingRating = new CourseRating();
            existingRating.setCourseId(courseId);
            existingRating.setUserId(userId);
            existingRating.setRatingValue(rating);
            existingRating.setCreatedAt(LocalDateTime.now());
            courseRatingMapper.insert(existingRating);
        }
        
        updateCourseAverageRating(courseId);
        return existingRating;
    }
    
    private void updateCourseAverageRating(int courseId) {
        QueryWrapper<CourseRating> ratingQuery = new QueryWrapper<CourseRating>().eq("course_id", courseId);
        List<CourseRating> ratings = courseRatingMapper.selectList(ratingQuery);
        
        int ratingCount = ratings.size();
        double average = 0.0;
        if (ratingCount > 0) {
            int sum = ratings.stream().mapToInt(CourseRating::getRatingValue).sum();
            average = (double) sum / ratingCount;
        }

        Course course = courseMapper.selectById(courseId);
        if (course != null) {
            course.setCourseAverageRating(new BigDecimal(average).setScale(2, RoundingMode.HALF_UP));
            course.setCourseRatingCount(ratingCount);
            courseMapper.updateById(course);
        }
    }

    @Override
    public UserCourseCollection collectCourse(int courseId, int userId) {
        // 校验课程是否存在
        if (courseMapper.selectById(courseId) == null) {
            return null;
        }
        // 校验用户是否存在
        if (userFeignClient.isUserExist(userId) == null || !userFeignClient.isUserExist(userId)) {
            return null;
        }
        QueryWrapper<UserCourseCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId).eq("user_id", userId);
        UserCourseCollection existing = userCourseCollectionMapper.selectOne(queryWrapper);
        if (existing != null) {
            return null;
        }
        UserCourseCollection collection = new UserCourseCollection();
        collection.setCourseId(courseId);
        collection.setUserId(userId);
        collection.setCreatedAt(LocalDateTime.now());
        userCourseCollectionMapper.insert(collection);
        return collection;
    }

    @Override
    public boolean removeCollection(int courseId, int userId) {
        QueryWrapper<UserCourseCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId).eq("user_id", userId);
        UserCourseCollection existing = userCourseCollectionMapper.selectOne(queryWrapper);
        if (existing == null) {
            return false;
        }
        userCourseCollectionMapper.delete(queryWrapper);
        return true;
    }

    @Override
    public PageVO<Course> getCollectedCoursesByUserId(int userId, int pageNum, int pageSize) {
        Page<UserCourseCollection> collectionPage = new Page<>(pageNum, pageSize);
        QueryWrapper<UserCourseCollection> collectionQuery = new QueryWrapper<>();
        collectionQuery.eq("user_id", userId).select("course_id");
        
        Page<UserCourseCollection> pagedCollections = userCourseCollectionMapper.selectPage(collectionPage, collectionQuery);

        List<Integer> courseIds = pagedCollections.getRecords().stream()
                .map(UserCourseCollection::getCourseId)
                .toList();

        if (courseIds.isEmpty()) {
            PageVO<Course> pageVO = new PageVO<>();
            pageVO.setTotal(0);
            pageVO.setPages(0);
            pageVO.setList(new ArrayList<>());
            return pageVO;
        }

        List<Course> courses = courseMapper.selectBatchIds(courseIds);

        PageVO<Course> pageVO = new PageVO<>();
        pageVO.setTotal(pagedCollections.getTotal());
        pageVO.setPages(pagedCollections.getPages());
        pageVO.setList(courses);
        return pageVO;
    }

    // --- Course Herb Management ---

    @Override
    @Transactional
    public boolean addHerbToCourse(int courseId, int herbId) {
        // 验证课程是否存在
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            return false;
        }

        // 验证中草药是否存在（通过Feign调用HerbInfo服务）
        Map<String, Object> herbInfo = herbInfoFeignClient.getHerbInfoById(herbId);
        if (herbInfo == null || herbInfo.get("herbId") == null) {
            return false;
        }

        // 检查是否已经存在关联
        QueryWrapper<CourseHerbLink> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId).eq("herb_id", herbId);
        if (courseHerbLinkMapper.selectCount(queryWrapper) > 0) {
            return false; // 关联已存在
        }

        // 创建新的关联
        CourseHerbLink link = new CourseHerbLink();
        link.setCourseId(courseId);
        link.setHerbId(herbId);
        
        return courseHerbLinkMapper.insert(link) > 0;
    }

    @Override
    @Transactional
    public boolean removeHerbFromCourse(int courseId, int herbId) {
        // 验证课程是否存在
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            return false;
        }

        // 删除关联
        QueryWrapper<CourseHerbLink> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId).eq("herb_id", herbId);
        
        return courseHerbLinkMapper.delete(queryWrapper) > 0;
    }

    @Override
    @Transactional
    public boolean updateCourseHerbs(int courseId, List<Integer> herbIds) {
        // 验证课程是否存在
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            return false;
        }

        // 验证所有中草药是否存在
        for (Integer herbId : herbIds) {
            Map<String, Object> herbInfo = herbInfoFeignClient.getHerbInfoById(herbId);
            if (herbInfo == null || herbInfo.get("herbId") == null) {
                return false; // 某个中草药不存在
            }
        }

        // 删除现有的所有关联
        QueryWrapper<CourseHerbLink> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("course_id", courseId);
        courseHerbLinkMapper.delete(deleteWrapper);

        // 创建新的关联
        for (Integer herbId : herbIds) {
            CourseHerbLink link = new CourseHerbLink();
            link.setCourseId(courseId);
            link.setHerbId(herbId);
            courseHerbLinkMapper.insert(link);
        }

        return true;
    }

    @Override
    public List<Integer> getCourseHerbIds(int courseId) {
        // 验证课程是否存在
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            return new ArrayList<>();
        }

        // 查询课程关联的所有中草药ID
        QueryWrapper<CourseHerbLink> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId).select("herb_id");
        List<CourseHerbLink> links = courseHerbLinkMapper.selectList(queryWrapper);
        
        return links.stream()
                .map(CourseHerbLink::getHerbId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isHerbLinkedToCourse(int courseId, int herbId) {
        QueryWrapper<CourseHerbLink> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId).eq("herb_id", herbId);
        return courseHerbLinkMapper.selectCount(queryWrapper) > 0;
    }
}
