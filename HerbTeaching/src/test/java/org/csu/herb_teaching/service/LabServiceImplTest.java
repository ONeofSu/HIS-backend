package org.csu.herb_teaching.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.herb_teaching.DTO.LabDTO;
import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.entity.Lab;
import org.csu.herb_teaching.mapper.CourseMapper;
import org.csu.herb_teaching.mapper.LabMapper;
import org.csu.herb_teaching.service.impl.LabServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LabServiceImpl单元测试")
class LabServiceImplTest {

    @Mock
    private LabMapper labMapper;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private LabServiceImpl labService;

    private Lab testLab;
    private LabDTO testLabDTO;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setCourseId(1);
        testCourse.setCourseName("测试课程");

        testLab = new Lab();
        testLab.setLabId(1);
        testLab.setCourseId(1);
        testLab.setLabName("测试实验");
        testLab.setLabSteps("步骤1：准备材料\n步骤2：进行实验\n步骤3：记录结果");
        testLab.setLabOrder(1);

        testLabDTO = new LabDTO();
        testLabDTO.setLabName("测试实验");
        testLabDTO.setLabSteps("步骤1：准备材料\n步骤2：进行实验\n步骤3：记录结果");
        testLabDTO.setLabOrder(1);
    }

    @Test
    @DisplayName("测试根据课程ID获取实验列表 - 正常情况")
    void testGetLabsByCourseId_Success() {
        // Arrange
        int courseId = 1;
        when(labMapper.selectList(any(QueryWrapper.class))).thenReturn(Collections.singletonList(testLab));

        // Act
        List<Lab> result = labService.getLabsByCourseId(courseId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("测试实验", result.get(0).getLabName());
        verify(labMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    @DisplayName("测试根据课程ID获取实验列表 - 无实验")
    void testGetLabsByCourseId_Empty() {
        // Arrange
        int courseId = 1;
        when(labMapper.selectList(any(QueryWrapper.class))).thenReturn(Collections.emptyList());

        // Act
        List<Lab> result = labService.getLabsByCourseId(courseId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("测试根据ID获取实验 - 正常情况")
    void testGetLabById_Success() {
        // Arrange
        int labId = 1;
        when(labMapper.selectById(labId)).thenReturn(testLab);

        // Act
        Lab result = labService.getLabById(labId);

        // Assert
        assertNotNull(result);
        assertEquals(labId, result.getLabId());
        assertEquals("测试实验", result.getLabName());
        verify(labMapper, times(1)).selectById(labId);
    }

    @Test
    @DisplayName("测试根据ID获取实验 - 不存在")
    void testGetLabById_NotFound() {
        // Arrange
        int labId = 999;
        when(labMapper.selectById(labId)).thenReturn(null);

        // Act
        Lab result = labService.getLabById(labId);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("测试创建实验 - 正常情况")
    void testCreateLab_Success() {
        // Arrange
        int courseId = 1;
        when(courseMapper.selectById(courseId)).thenReturn(testCourse);
        when(labMapper.selectMaxLabOrderByCourseId(courseId)).thenReturn(null);
        when(labMapper.insert(any(Lab.class))).thenAnswer(invocation -> {
            Lab lab = invocation.getArgument(0);
            lab.setLabId(1);
            return 1;
        });

        // Act
        Lab result = labService.createLab(courseId, testLabDTO);

        // Assert
        assertNotNull(result);
        assertEquals(courseId, result.getCourseId());
        assertEquals("测试实验", result.getLabName());
        assertEquals(1, result.getLabOrder());
        verify(labMapper, times(1)).insert(any(Lab.class));
    }

    @Test
    @DisplayName("测试创建实验 - 课程不存在")
    void testCreateLab_CourseNotFound() {
        // Arrange
        int courseId = 999;
        when(courseMapper.selectById(courseId)).thenReturn(null);

        // Act
        Lab result = labService.createLab(courseId, testLabDTO);

        // Assert
        assertNull(result);
        verify(labMapper, never()).insert(any(Lab.class));
    }

    @Test
    @DisplayName("测试创建实验 - 已有实验，自动分配顺序")
    void testCreateLab_WithExistingLabs() {
        // Arrange
        int courseId = 1;
        when(courseMapper.selectById(courseId)).thenReturn(testCourse);
        when(labMapper.selectMaxLabOrderByCourseId(courseId)).thenReturn(3);
        when(labMapper.insert(any(Lab.class))).thenAnswer(invocation -> {
            Lab lab = invocation.getArgument(0);
            lab.setLabId(2);
            return 1;
        });

        // Act
        Lab result = labService.createLab(courseId, testLabDTO);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.getLabOrder()); // 应该是3+1=4
        verify(labMapper, times(1)).insert(any(Lab.class));
    }

    @Test
    @DisplayName("测试更新实验 - 正常情况")
    void testUpdateLab_Success() {
        // Arrange
        int labId = 1;
        testLabDTO.setLabName("更新后的实验名称");
        when(labMapper.selectById(labId)).thenReturn(testLab);
        when(labMapper.updateById(any(Lab.class))).thenReturn(1);

        // Act
        Lab result = labService.updateLab(labId, testLabDTO);

        // Assert
        assertNotNull(result);
        assertEquals("更新后的实验名称", result.getLabName());
        verify(labMapper, times(1)).updateById(any(Lab.class));
    }

    @Test
    @DisplayName("测试更新实验 - 实验不存在")
    void testUpdateLab_NotFound() {
        // Arrange
        int labId = 999;
        when(labMapper.selectById(labId)).thenReturn(null);

        // Act
        Lab result = labService.updateLab(labId, testLabDTO);

        // Assert
        assertNull(result);
        verify(labMapper, never()).updateById(any(Lab.class));
    }

    @Test
    @DisplayName("测试更新实验 - 部分字段更新")
    void testUpdateLab_PartialUpdate() {
        // Arrange
        int labId = 1;
        LabDTO partialDTO = new LabDTO();
        partialDTO.setLabName("只更新名称");
        // 其他字段为null或0，不更新

        when(labMapper.selectById(labId)).thenReturn(testLab);
        when(labMapper.updateById(any(Lab.class))).thenReturn(1);

        // Act
        Lab result = labService.updateLab(labId, partialDTO);

        // Assert
        assertNotNull(result);
        assertEquals("只更新名称", result.getLabName());
        // 其他字段应该保持不变
        verify(labMapper, times(1)).updateById(any(Lab.class));
    }

    @Test
    @DisplayName("测试删除实验 - 正常情况")
    void testDeleteLab_Success() {
        // Arrange
        int labId = 1;
        when(labMapper.selectById(labId)).thenReturn(testLab);
        when(labMapper.deleteById(labId)).thenReturn(1);

        // Act
        boolean result = labService.deleteLab(labId);

        // Assert
        assertTrue(result);
        verify(labMapper, times(1)).deleteById(labId);
    }

    @Test
    @DisplayName("测试删除实验 - 实验不存在")
    void testDeleteLab_NotFound() {
        // Arrange
        int labId = 999;
        when(labMapper.selectById(labId)).thenReturn(null);

        // Act
        boolean result = labService.deleteLab(labId);

        // Assert
        assertFalse(result);
        verify(labMapper, never()).deleteById(anyInt());
    }
}

