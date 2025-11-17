package org.csu.herb_teaching.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.herb_teaching.DTO.CourseResourceDTO;
import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.entity.CourseResource;
import org.csu.herb_teaching.mapper.CourseMapper;
import org.csu.herb_teaching.mapper.CourseResourceMapper;
import org.csu.herb_teaching.service.impl.ResourceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ResourceServiceImpl单元测试")
class ResourceServiceImplTest {

    @Mock
    private CourseResourceMapper courseResourceMapper;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private ResourceServiceImpl resourceService;

    private CourseResource testResource;
    private CourseResourceDTO testResourceDTO;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setCourseId(1);
        testCourse.setCourseName("测试课程");

        testResource = new CourseResource();
        testResource.setCourseResourceId(1);
        testResource.setCourseId(1);
        testResource.setCourseResourceType(0); // 视频
        testResource.setCourseResourceOrder(1);
        testResource.setCourseResourceTitle("测试资源");
        testResource.setCourseResourceContent("http://example.com/video.mp4");
        testResource.setCourseResourceMetadata("{\"duration\":\"10:30\"}");
        testResource.setCourseResourceTime(LocalDateTime.now());
        testResource.setCourseResourceIsvalid(true);

        testResourceDTO = new CourseResourceDTO();
        testResourceDTO.setCourseResourceType(0);
        testResourceDTO.setCourseResourceOrder(1);
        testResourceDTO.setCourseResourceTitle("测试资源");
        testResourceDTO.setCourseResourceContent("http://example.com/video.mp4");
        testResourceDTO.setCourseResourceMetadata("{\"duration\":\"10:30\"}");
    }

    @Test
    @DisplayName("测试根据课程ID获取资源列表 - 正常情况")
    void testGetResourcesByCourseId_Success() {
        // Arrange
        int courseId = 1;
        when(courseResourceMapper.selectList(any(QueryWrapper.class))).thenReturn(Collections.singletonList(testResource));

        // Act
        List<CourseResource> result = resourceService.getResourcesByCourseId(courseId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试资源", result.get(0).getCourseResourceTitle());
        verify(courseResourceMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    @DisplayName("测试根据课程ID获取资源列表 - 无资源")
    void testGetResourcesByCourseId_Empty() {
        // Arrange
        int courseId = 1;
        when(courseResourceMapper.selectList(any(QueryWrapper.class))).thenReturn(Collections.emptyList());

        // Act
        List<CourseResource> result = resourceService.getResourcesByCourseId(courseId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("测试根据ID获取资源 - 正常情况")
    void testGetResourceById_Success() {
        // Arrange
        int resourceId = 1;
        when(courseResourceMapper.selectById(resourceId)).thenReturn(testResource);

        // Act
        CourseResource result = resourceService.getResourceById(resourceId);

        // Assert
        assertNotNull(result);
        assertEquals(resourceId, result.getCourseResourceId());
        assertEquals("测试资源", result.getCourseResourceTitle());
        verify(courseResourceMapper, times(1)).selectById(resourceId);
    }

    @Test
    @DisplayName("测试根据ID获取资源 - 不存在")
    void testGetResourceById_NotFound() {
        // Arrange
        int resourceId = 999;
        when(courseResourceMapper.selectById(resourceId)).thenReturn(null);

        // Act
        CourseResource result = resourceService.getResourceById(resourceId);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("测试创建资源 - 正常情况")
    void testCreateResource_Success() {
        // Arrange
        int courseId = 1;
        when(courseMapper.selectById(courseId)).thenReturn(testCourse);
        when(courseResourceMapper.selectMaxCourseResourceOrderByCourseId(courseId)).thenReturn(null);
        when(courseResourceMapper.insert(any(CourseResource.class))).thenAnswer(invocation -> {
            CourseResource resource = invocation.getArgument(0);
            resource.setCourseResourceId(1);
            return 1;
        });

        // Act
        CourseResource result = resourceService.createResource(courseId, testResourceDTO);

        // Assert
        assertNotNull(result);
        assertEquals(courseId, result.getCourseId());
        assertEquals("测试资源", result.getCourseResourceTitle());
        assertEquals(1, result.getCourseResourceOrder());
        assertTrue(result.isCourseResourceIsvalid());
        assertNotNull(result.getCourseResourceTime());
        verify(courseResourceMapper, times(1)).insert(any(CourseResource.class));
    }

    @Test
    @DisplayName("测试创建资源 - 课程不存在")
    void testCreateResource_CourseNotFound() {
        // Arrange
        int courseId = 999;
        when(courseMapper.selectById(courseId)).thenReturn(null);

        // Act
        CourseResource result = resourceService.createResource(courseId, testResourceDTO);

        // Assert
        assertNull(result);
        verify(courseResourceMapper, never()).insert(any(CourseResource.class));
    }

    @Test
    @DisplayName("测试创建资源 - 已有资源，自动分配顺序")
    void testCreateResource_WithExistingResources() {
        // Arrange
        int courseId = 1;
        when(courseMapper.selectById(courseId)).thenReturn(testCourse);
        when(courseResourceMapper.selectMaxCourseResourceOrderByCourseId(courseId)).thenReturn(2);
        when(courseResourceMapper.insert(any(CourseResource.class))).thenAnswer(invocation -> {
            CourseResource resource = invocation.getArgument(0);
            resource.setCourseResourceId(2);
            return 1;
        });

        // Act
        CourseResource result = resourceService.createResource(courseId, testResourceDTO);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getCourseResourceOrder()); // 应该是2+1=3
        verify(courseResourceMapper, times(1)).insert(any(CourseResource.class));
    }

    @Test
    @DisplayName("测试更新资源 - 正常情况")
    void testUpdateResource_Success() {
        // Arrange
        int resourceId = 1;
        testResourceDTO.setCourseResourceTitle("更新后的资源名称");
        when(courseResourceMapper.selectById(resourceId)).thenReturn(testResource);
        when(courseResourceMapper.updateById(any(CourseResource.class))).thenReturn(1);

        // Act
        CourseResource result = resourceService.updateResource(resourceId, testResourceDTO);

        // Assert
        assertNotNull(result);
        assertEquals("更新后的资源名称", result.getCourseResourceTitle());
        verify(courseResourceMapper, times(1)).updateById(any(CourseResource.class));
    }

    @Test
    @DisplayName("测试更新资源 - 资源不存在")
    void testUpdateResource_NotFound() {
        // Arrange
        int resourceId = 999;
        when(courseResourceMapper.selectById(resourceId)).thenReturn(null);

        // Act
        CourseResource result = resourceService.updateResource(resourceId, testResourceDTO);

        // Assert
        assertNull(result);
        verify(courseResourceMapper, never()).updateById(any(CourseResource.class));
    }

    @Test
    @DisplayName("测试更新资源 - 部分字段更新")
    void testUpdateResource_PartialUpdate() {
        // Arrange
        int resourceId = 1;
        CourseResourceDTO partialDTO = new CourseResourceDTO();
        partialDTO.setCourseResourceTitle("只更新标题");
        // 其他字段为null，不更新

        when(courseResourceMapper.selectById(resourceId)).thenReturn(testResource);
        when(courseResourceMapper.updateById(any(CourseResource.class))).thenReturn(1);

        // Act
        CourseResource result = resourceService.updateResource(resourceId, partialDTO);

        // Assert
        assertNotNull(result);
        assertEquals("只更新标题", result.getCourseResourceTitle());
        // 其他字段应该保持不变
        verify(courseResourceMapper, times(1)).updateById(any(CourseResource.class));
    }

    @Test
    @DisplayName("测试更新资源 - 更新顺序")
    void testUpdateResource_UpdateOrder() {
        // Arrange
        int resourceId = 1;
        CourseResourceDTO orderDTO = new CourseResourceDTO();
        orderDTO.setCourseResourceOrder(5);

        when(courseResourceMapper.selectById(resourceId)).thenReturn(testResource);
        when(courseResourceMapper.updateById(any(CourseResource.class))).thenReturn(1);

        // Act
        CourseResource result = resourceService.updateResource(resourceId, orderDTO);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getCourseResourceOrder());
        verify(courseResourceMapper, times(1)).updateById(any(CourseResource.class));
    }

    @Test
    @DisplayName("测试删除资源 - 正常情况")
    void testDeleteResource_Success() {
        // Arrange
        int resourceId = 1;
        when(courseResourceMapper.selectById(resourceId)).thenReturn(testResource);
        when(courseResourceMapper.deleteById(resourceId)).thenReturn(1);

        // Act
        boolean result = resourceService.deleteResource(resourceId);

        // Assert
        assertTrue(result);
        verify(courseResourceMapper, times(1)).deleteById(resourceId);
    }

    @Test
    @DisplayName("测试删除资源 - 资源不存在")
    void testDeleteResource_NotFound() {
        // Arrange
        int resourceId = 999;
        when(courseResourceMapper.selectById(resourceId)).thenReturn(null);

        // Act
        boolean result = resourceService.deleteResource(resourceId);

        // Assert
        assertFalse(result);
        verify(courseResourceMapper, never()).deleteById(anyInt());
    }
}

