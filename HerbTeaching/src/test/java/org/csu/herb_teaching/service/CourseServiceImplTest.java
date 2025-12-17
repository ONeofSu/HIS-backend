package org.csu.herb_teaching.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.csu.herb_teaching.DTO.CourseDTO;
import org.csu.herb_teaching.VO.CourseDetailVO;
import org.csu.herb_teaching.VO.PageVO;
import org.csu.herb_teaching.VO.UserVO;
import org.csu.herb_teaching.entity.*;
import org.csu.herb_teaching.feign.HerbInfoFeignClient;
import org.csu.herb_teaching.feign.UserFeignClient;
import org.csu.herb_teaching.mapper.*;
import org.csu.herb_teaching.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseServiceImpl单元测试")
class CourseServiceImplTest {

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private LabMapper labMapper;

    @Mock
    private CourseResourceMapper courseResourceMapper;

    @Mock
    private CourseRatingMapper courseRatingMapper;

    @Mock
    private UserCourseCollectionMapper userCourseCollectionMapper;

    @Mock
    private CourseHerbLinkMapper courseHerbLinkMapper;

    @Mock
    private UserFeignClient userFeignClient;

    @Mock
    private HerbInfoFeignClient herbInfoFeignClient;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course testCourse;
    private CourseDTO testCourseDTO;
    private UserVO testUserVO;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setCourseId(1);
        testCourse.setCourseName("测试课程");
        testCourse.setCoverImageUrl("http://example.com/image.jpg");
        testCourse.setCourseType(1);
        testCourse.setCourseObject(0);
        testCourse.setTeacherId(100);
        testCourse.setCourseStartTime(LocalDateTime.now());
        testCourse.setCourseEndTime(LocalDateTime.now().plusDays(30));
        testCourse.setCourseDes("这是一门测试课程");
        testCourse.setCourseAverageRating(BigDecimal.ZERO);
        testCourse.setCourseRatingCount(0);

        testCourseDTO = new CourseDTO();
        testCourseDTO.setCourseName("测试课程");
        testCourseDTO.setCoverImageUrl("http://example.com/image.jpg");
        testCourseDTO.setCourseType(1);
        testCourseDTO.setCourseObject(0);
        testCourseDTO.setTeacherId(100);
        testCourseDTO.setCourseStartTime(LocalDateTime.now());
        testCourseDTO.setCourseEndTime(LocalDateTime.now().plusDays(30));
        testCourseDTO.setCourseDes("这是一门测试课程");

        testUserVO = new UserVO();
        testUserVO.setId(100);
        testUserVO.setUsername("测试教师");
        testUserVO.setAvatarUrl("http://example.com/avatar.jpg");
    }

    @Test
    @DisplayName("测试获取课程列表 - 正常情况")
    void testGetCourseList_Success() {
        // Arrange
        int pageNum = 1;
        int pageSize = 10;
        String keyword = "测试";
        int courseType = 1;
        int courseObject = 0;

        Page<Course> mockPage = new Page<>(pageNum, pageSize);
        mockPage.setTotal(1);
        mockPage.setPages(1);
        mockPage.setRecords(Collections.singletonList(testCourse));

        when(courseMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn(mockPage);

        // Act
        PageVO<Course> result = courseService.getCourseList(pageNum, pageSize, keyword, courseType, courseObject);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        assertEquals(1, result.getPages());
        assertEquals(1, result.getList().size());
        assertEquals("测试课程", result.getList().get(0).getCourseName());
        verify(courseMapper, times(1)).selectPage(any(Page.class), any(QueryWrapper.class));
    }

    @Test
    @DisplayName("测试获取课程列表 - 无关键词")
    void testGetCourseList_NoKeyword() {
        // Arrange
        Page<Course> mockPage = new Page<>(1, 10);
        mockPage.setTotal(0);
        mockPage.setPages(0);
        mockPage.setRecords(Collections.emptyList());

        when(courseMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn(mockPage);

        // Act
        PageVO<Course> result = courseService.getCourseList(1, 10, null, 0, 0);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertEquals(0, result.getList().size());
    }

    @Test
    @DisplayName("测试获取课程详情 - 正常情况")
    void testGetCourseDetail_Success() {
        // Arrange
        int courseId = 1;
        when(courseMapper.selectById(courseId)).thenReturn(testCourse);
        when(userFeignClient.getUserInfoById(100)).thenReturn(testUserVO);
        when(labMapper.selectList(any(QueryWrapper.class))).thenReturn(Collections.emptyList());
        when(courseResourceMapper.selectList(any(QueryWrapper.class))).thenReturn(Collections.emptyList());
        when(courseHerbLinkMapper.selectList(any(QueryWrapper.class))).thenReturn(Collections.emptyList());

        // Act
        CourseDetailVO result = courseService.getCourseDetail(courseId);

        // Assert
        assertNotNull(result);
        assertEquals(courseId, result.getCourseId());
        assertEquals("测试课程", result.getCourseName());
        assertEquals("测试教师", result.getTeacherName());
        verify(courseMapper, times(1)).selectById(courseId);
        verify(userFeignClient, times(1)).getUserInfoById(100);
    }

    @Test
    @DisplayName("测试获取课程详情 - 课程不存在")
    void testGetCourseDetail_NotFound() {
        // Arrange
        int courseId = 999;
        when(courseMapper.selectById(courseId)).thenReturn(null);

        // Act
        CourseDetailVO result = courseService.getCourseDetail(courseId);

        // Assert
        assertNull(result);
        verify(courseMapper, times(1)).selectById(courseId);
        verify(userFeignClient, never()).getUserInfoById(anyInt());
    }

    @Test
    @DisplayName("测试创建课程 - 正常情况")
    void testCreateCourse_Success() {
        // Arrange
        when(courseMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);
        when(userFeignClient.isUserRealTeacher(100)).thenReturn(true);
        when(courseMapper.insert(any(Course.class))).thenAnswer(invocation -> {
            Course course = invocation.getArgument(0);
            course.setCourseId(1);
            return 1;
        });

        // Act
        Course result = courseService.createCourse(testCourseDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getCourseId());
        assertEquals("测试课程", result.getCourseName());
        assertEquals(BigDecimal.ZERO, result.getCourseAverageRating());
        assertEquals(0, result.getCourseRatingCount());
        verify(courseMapper, times(1)).insert(any(Course.class));
    }

    @Test
    @DisplayName("测试创建课程 - 课程名已存在")
    void testCreateCourse_NameExists() {
        // Arrange
        when(courseMapper.selectCount(any(QueryWrapper.class))).thenReturn(1L);

        // Act
        Course result = courseService.createCourse(testCourseDTO);

        // Assert
        assertNull(result);
        verify(courseMapper, never()).insert(any(Course.class));
    }

    @Test
    @DisplayName("测试创建课程 - 教师ID无效（isTeacher为null）")
    void testCreateCourse_InvalidTeacher_Null() {
        // Arrange
        when(courseMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);
        when(userFeignClient.isUserRealTeacher(100)).thenReturn(null);

        // Act
        Course result = courseService.createCourse(testCourseDTO);

        // Assert
        assertNull(result);
        verify(courseMapper, never()).insert(any(Course.class));
    }

    @Test
    @DisplayName("测试创建课程 - 教师ID无效（isTeacher为false）")
    void testCreateCourse_InvalidTeacher_False() {
        // Arrange
        when(courseMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);
        when(userFeignClient.isUserRealTeacher(100)).thenReturn(false);

        // Act
        Course result = courseService.createCourse(testCourseDTO);

        // Assert
        assertNull(result);
        verify(courseMapper, never()).insert(any(Course.class));
    }

    @Test
    @DisplayName("测试创建课程 - 边界条件：课程名为空字符串")
    void testCreateCourse_EmptyCourseName() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName(""); // 空字符串
        courseDTO.setTeacherId(100);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);

        // Mock: 空字符串课程名不存在
        when(courseMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);
        when(userFeignClient.isUserRealTeacher(100)).thenReturn(true);
        when(courseMapper.insert(any(Course.class))).thenAnswer(invocation -> {
            Course course = invocation.getArgument(0);
            course.setCourseId(1);
            return 1;
        });

        // Act
        Course result = courseService.createCourse(courseDTO);

        // Assert
        // 根据业务逻辑，空字符串课程名可能被允许或拒绝
        // 这里假设允许创建，实际应根据业务需求调整
        assertNotNull(result);
    }

    @Test
    @DisplayName("测试创建课程 - 边界条件：课程名包含特殊字符")
    void testCreateCourse_SpecialCharactersInName() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("测试课程@#$%");
        courseDTO.setTeacherId(100);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);

        // Mock: 特殊字符课程名不存在
        when(courseMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);
        when(userFeignClient.isUserRealTeacher(100)).thenReturn(true);
        when(courseMapper.insert(any(Course.class))).thenAnswer(invocation -> {
            Course course = invocation.getArgument(0);
            course.setCourseId(1);
            return 1;
        });

        // Act
        Course result = courseService.createCourse(courseDTO);

        // Assert
        assertNotNull(result);
        assertEquals("测试课程@#$%", result.getCourseName());
    }

    @Test
    @DisplayName("测试创建课程 - 验证初始评分和评分数量")
    void testCreateCourse_InitialRatingValues() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseName("新课程");
        courseDTO.setTeacherId(100);
        courseDTO.setCourseType(1);
        courseDTO.setCourseObject(0);

        when(courseMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);
        when(userFeignClient.isUserRealTeacher(100)).thenReturn(true);
        when(courseMapper.insert(any(Course.class))).thenAnswer(invocation -> {
            Course course = invocation.getArgument(0);
            course.setCourseId(1);
            return 1;
        });

        // Act
        Course result = courseService.createCourse(courseDTO);

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result.getCourseAverageRating());
        assertEquals(0, result.getCourseRatingCount());
    }

    @Test
    @DisplayName("测试更新课程 - 正常情况")
    void testUpdateCourse_Success() {
        // Arrange
        testCourseDTO.setCourseId(1);
        when(courseMapper.selectById(1)).thenReturn(testCourse);
        when(courseMapper.updateById(any(Course.class))).thenReturn(1);

        // Act
        Course result = courseService.updateCourse(testCourseDTO);

        // Assert
        assertNotNull(result);
        verify(courseMapper, times(1)).updateById(any(Course.class));
    }

    @Test
    @DisplayName("测试更新课程 - 课程不存在")
    void testUpdateCourse_NotFound() {
        // Arrange
        testCourseDTO.setCourseId(999);
        when(courseMapper.selectById(999)).thenReturn(null);

        // Act
        Course result = courseService.updateCourse(testCourseDTO);

        // Assert
        assertNull(result);
        verify(courseMapper, never()).updateById(any(Course.class));
    }

    @Test
    @DisplayName("测试删除课程 - 正常情况")
    void testDeleteCourse_Success() {
        // Arrange
        int courseId = 1;
        when(courseMapper.selectById(courseId)).thenReturn(testCourse);
        when(labMapper.delete(any(QueryWrapper.class))).thenReturn(1);
        when(courseResourceMapper.delete(any(QueryWrapper.class))).thenReturn(1);
        when(courseRatingMapper.delete(any(QueryWrapper.class))).thenReturn(1);
        when(userCourseCollectionMapper.delete(any(QueryWrapper.class))).thenReturn(1);
        when(courseHerbLinkMapper.delete(any(QueryWrapper.class))).thenReturn(1);
        when(courseMapper.deleteById(courseId)).thenReturn(1);

        // Act
        boolean result = courseService.deleteCourse(courseId);

        // Assert
        assertTrue(result);
        verify(courseMapper, times(1)).deleteById(courseId);
    }

    @Test
    @DisplayName("测试删除课程 - 课程不存在")
    void testDeleteCourse_NotFound() {
        // Arrange
        int courseId = 999;
        when(courseMapper.selectById(courseId)).thenReturn(null);

        // Act
        boolean result = courseService.deleteCourse(courseId);

        // Assert
        assertFalse(result);
        verify(courseMapper, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("测试课程评分 - 正常情况")
    void testRateCourse_Success() {
        // Arrange
        int courseId = 1;
        int userId = 200;
        int rating = 5;

        when(courseMapper.selectById(courseId)).thenReturn(testCourse);
        when(courseRatingMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(courseRatingMapper.insert(any(CourseRating.class))).thenReturn(1);
        when(courseRatingMapper.selectList(any(QueryWrapper.class))).thenReturn(Collections.emptyList());
        when(courseMapper.updateById(any(Course.class))).thenReturn(1);

        // Act
        CourseRating result = courseService.rateCourse(courseId, userId, rating);

        // Assert
        assertNotNull(result);
        assertEquals(courseId, result.getCourseId());
        assertEquals(userId, result.getUserId());
        assertEquals(rating, result.getRatingValue());
        verify(courseRatingMapper, times(1)).insert(any(CourseRating.class));
    }

    @Test
    @DisplayName("测试课程评分 - 评分值无效")
    void testRateCourse_InvalidRating() {
        // Arrange
        int courseId = 1;
        int userId = 200;
        int rating = 10; // 无效评分

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            courseService.rateCourse(courseId, userId, rating);
        });
        verify(courseMapper, never()).selectById(anyInt());
    }

    @Test
    @DisplayName("测试课程评分 - 更新已有评分")
    void testRateCourse_UpdateExisting() {
        // Arrange
        int courseId = 1;
        int userId = 200;
        int rating = 4;

        CourseRating existingRating = new CourseRating();
        existingRating.setCourseId(courseId);
        existingRating.setUserId(userId);
        existingRating.setRatingValue(3);

        when(courseMapper.selectById(courseId)).thenReturn(testCourse);
        when(courseRatingMapper.selectOne(any(QueryWrapper.class))).thenReturn(existingRating);
        when(courseRatingMapper.updateById(any(CourseRating.class))).thenReturn(1);
        when(courseRatingMapper.selectList(any(QueryWrapper.class))).thenReturn(Collections.singletonList(existingRating));
        when(courseMapper.updateById(any(Course.class))).thenReturn(1);

        // Act
        CourseRating result = courseService.rateCourse(courseId, userId, rating);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.getRatingValue());
        verify(courseRatingMapper, times(1)).updateById(any(CourseRating.class));
        verify(courseRatingMapper, never()).insert(any(CourseRating.class));
    }

    @Test
    @DisplayName("测试收藏课程 - 正常情况")
    void testCollectCourse_Success() {
        // Arrange
        int courseId = 1;
        int userId = 200;

        when(courseMapper.selectById(courseId)).thenReturn(testCourse);
        when(userFeignClient.isUserExist(userId)).thenReturn(true);
        when(userCourseCollectionMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(userCourseCollectionMapper.insert(any(UserCourseCollection.class))).thenReturn(1);

        // Act
        UserCourseCollection result = courseService.collectCourse(courseId, userId);

        // Assert
        assertNotNull(result);
        assertEquals(courseId, result.getCourseId());
        assertEquals(userId, result.getUserId());
        verify(userCourseCollectionMapper, times(1)).insert(any(UserCourseCollection.class));
    }

    @Test
    @DisplayName("测试收藏课程 - 课程不存在")
    void testCollectCourse_CourseNotFound() {
        // Arrange
        int courseId = 999;
        int userId = 200;

        when(courseMapper.selectById(courseId)).thenReturn(null);

        // Act
        UserCourseCollection result = courseService.collectCourse(courseId, userId);

        // Assert
        assertNull(result);
        verify(userCourseCollectionMapper, never()).insert(any(UserCourseCollection.class));
    }

    @Test
    @DisplayName("测试收藏课程 - 已收藏")
    void testCollectCourse_AlreadyCollected() {
        // Arrange
        int courseId = 1;
        int userId = 200;

        UserCourseCollection existing = new UserCourseCollection();
        existing.setCourseId(courseId);
        existing.setUserId(userId);

        when(courseMapper.selectById(courseId)).thenReturn(testCourse);
        when(userFeignClient.isUserExist(userId)).thenReturn(true);
        when(userCourseCollectionMapper.selectOne(any(QueryWrapper.class))).thenReturn(existing);

        // Act
        UserCourseCollection result = courseService.collectCourse(courseId, userId);

        // Assert
        assertNull(result);
        verify(userCourseCollectionMapper, never()).insert(any(UserCourseCollection.class));
    }

    @Test
    @DisplayName("测试取消收藏 - 正常情况")
    void testRemoveCollection_Success() {
        // Arrange
        int courseId = 1;
        int userId = 200;

        UserCourseCollection existing = new UserCourseCollection();
        existing.setCourseId(courseId);
        existing.setUserId(userId);

        when(userCourseCollectionMapper.selectOne(any(QueryWrapper.class))).thenReturn(existing);
        when(userCourseCollectionMapper.delete(any(QueryWrapper.class))).thenReturn(1);

        // Act
        boolean result = courseService.removeCollection(courseId, userId);

        // Assert
        assertTrue(result);
        verify(userCourseCollectionMapper, times(1)).delete(any(QueryWrapper.class));
    }

    @Test
    @DisplayName("测试添加中草药到课程 - 正常情况")
    void testAddHerbToCourse_Success() {
        // Arrange
        int courseId = 1;
        int herbId = 10;

        Map<String, Object> herbInfo = new HashMap<>();
        herbInfo.put("herbId", herbId);
        herbInfo.put("herbName", "人参");

        when(courseMapper.selectById(courseId)).thenReturn(testCourse);
        when(herbInfoFeignClient.getHerbInfoById(herbId)).thenReturn(herbInfo);
        when(courseHerbLinkMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);
        when(courseHerbLinkMapper.insert(any(CourseHerbLink.class))).thenReturn(1);

        // Act
        boolean result = courseService.addHerbToCourse(courseId, herbId);

        // Assert
        assertTrue(result);
        verify(courseHerbLinkMapper, times(1)).insert(any(CourseHerbLink.class));
    }

    @Test
    @DisplayName("测试添加中草药到课程 - 中草药不存在")
    void testAddHerbToCourse_HerbNotFound() {
        // Arrange
        int courseId = 1;
        int herbId = 999;

        when(courseMapper.selectById(courseId)).thenReturn(testCourse);
        when(herbInfoFeignClient.getHerbInfoById(herbId)).thenReturn(null);

        // Act
        boolean result = courseService.addHerbToCourse(courseId, herbId);

        // Assert
        assertFalse(result);
        verify(courseHerbLinkMapper, never()).insert(any(CourseHerbLink.class));
    }

    @Test
    @DisplayName("测试检查用户是否已评分")
    void testHasUserRatedCourse() {
        // Arrange
        int courseId = 1;
        int userId = 200;

        when(courseRatingMapper.selectCount(any(QueryWrapper.class))).thenReturn(1L);

        // Act
        boolean result = courseService.hasUserRatedCourse(courseId, userId);

        // Assert
        assertTrue(result);
        verify(courseRatingMapper, times(1)).selectCount(any(QueryWrapper.class));
    }

    @Test
    @DisplayName("测试检查用户是否已收藏")
    void testHasUserCollectedCourse() {
        // Arrange
        int courseId = 1;
        int userId = 200;

        when(userCourseCollectionMapper.selectCount(any(QueryWrapper.class))).thenReturn(1L);

        // Act
        boolean result = courseService.hasUserCollectedCourse(courseId, userId);

        // Assert
        assertTrue(result);
        verify(userCourseCollectionMapper, times(1)).selectCount(any(QueryWrapper.class));
    }
}

