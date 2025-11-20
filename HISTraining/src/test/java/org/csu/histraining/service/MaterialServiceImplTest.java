package org.csu.histraining.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.csu.histraining.DTO.ContentDTO;
import org.csu.histraining.DTO.MaterialDTO;
import org.csu.histraining.DTO.UpdateMaterialDTO;
import org.csu.histraining.VO.ContentVO;
import org.csu.histraining.VO.MaterialVO;
import org.csu.histraining.VO.SimpleMaterialVO;
import org.csu.histraining.entity.Content;
import org.csu.histraining.entity.Material;
import org.csu.histraining.mapper.ContentMapper;
import org.csu.histraining.mapper.MaterialMapper;
import org.csu.histraining.model.MaterialModel;
import org.csu.histraining.service.HerbService;
import org.csu.histraining.service.UserService;
import org.csu.histraining.service.impl.MaterialServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private HerbService herbService;

    @Mock
    private MaterialMapper materialMapper;

    @Mock
    private ContentMapper contentMapper;

    @InjectMocks
    private MaterialServiceImpl materialService;

    private Material testMaterial;
    private Content testContent;
    private MaterialDTO testMaterialDTO;
    private UpdateMaterialDTO testUpdateMaterialDTO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testMaterial = new Material();
        testMaterial.setId(1);
        testMaterial.setTitle("测试材料");
        testMaterial.setType("类型1");
        testMaterial.setUserId(1);
        testMaterial.setHerbId(1);
        testMaterial.setCount(0);
        testMaterial.setIsvalid(true);
        testMaterial.setTime(new Timestamp(System.currentTimeMillis()));

        testContent = new Content();
        testContent.setId(1);
        testContent.setMaterialId(1);
        testContent.setType(Content.TEXT_TYPE);
        testContent.setDes("测试描述");
        testContent.setSortOrder(1);
        testContent.setIsvalid(true);

        // 初始化 DTO
        testMaterialDTO = new MaterialDTO();
        testMaterialDTO.setTitle("测试材料");
        testMaterialDTO.setType("类型1");
        testMaterialDTO.setHerbName("中药名称");
        testMaterialDTO.setDes("描述");

        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setOrder(1);
        contentDTO.setDes("内容描述");
        contentDTO.setType("文字");
        testMaterialDTO.setContents(Arrays.asList(contentDTO));

        testUpdateMaterialDTO = new UpdateMaterialDTO();
        testUpdateMaterialDTO.setId(1);
        testUpdateMaterialDTO.setTitle("更新材料");
        testUpdateMaterialDTO.setType("类型2");
        testUpdateMaterialDTO.setHerbName("中药名称");
        testUpdateMaterialDTO.setContents(Arrays.asList(contentDTO));
    }

    // ==================== Material Tests ====================

    @Test
    void testAddMaterial_Success() {
        // Given
        when(userService.isUserIdExist(anyInt())).thenReturn(true);
        when(herbService.isHerbIdValid(anyInt())).thenReturn(true);
        when(materialMapper.insert(any(Material.class))).thenAnswer(invocation -> {
            Material material = invocation.getArgument(0);
            material.setId(1);
            return 1;
        });

        // When
        int result = materialService.addMaterial(testMaterial);

        // Then
        assertEquals(1, result);
        verify(materialMapper).insert(any(Material.class));
        assertEquals(0, testMaterial.getCount());
        assertTrue(testMaterial.isIsvalid());
    }

    @Test
    void testAddMaterial_InvalidTitle() {
        // Given
        testMaterial.setTitle("");

        // When
        int result = materialService.addMaterial(testMaterial);

        // Then
        assertEquals(-1, result);
        verify(materialMapper, never()).insert(any(Material.class));
    }

    @Test
    void testAddMaterial_InvalidType() {
        // Given
        testMaterial.setType(null);

        // When
        int result = materialService.addMaterial(testMaterial);

        // Then
        assertEquals(-1, result);
        verify(materialMapper, never()).insert(any(Material.class));
    }

    @Test
    void testAddMaterial_UserNotExist() {
        // Given
        when(userService.isUserIdExist(anyInt())).thenReturn(false);

        // When
        int result = materialService.addMaterial(testMaterial);

        // Then
        assertEquals(-1, result);
        verify(materialMapper, never()).insert(any(Material.class));
    }

    @Test
    void testAddMaterial_HerbNotExist() {
        // Given
        when(userService.isUserIdExist(anyInt())).thenReturn(true);
        when(herbService.isHerbIdValid(anyInt())).thenReturn(false);

        // When
        int result = materialService.addMaterial(testMaterial);

        // Then
        assertEquals(-1, result);
        verify(materialMapper, never()).insert(any(Material.class));
    }

    @Test
    void testGetAllMaterial() {
        // Given
        List<Material> expectedMaterials = Arrays.asList(testMaterial);
        when(materialMapper.selectList(any(QueryWrapper.class))).thenReturn(expectedMaterials);

        // When
        List<Material> result = materialService.getAllMaterial();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMaterial.getId(), result.get(0).getId());
        verify(materialMapper).selectList(any(QueryWrapper.class));
    }

    @Test
    void testGetAllMaterialDividePages() {
        // Given
        List<Material> expectedMaterials = Arrays.asList(testMaterial);
        Page<Material> page = new Page<>();
        page.setRecords(expectedMaterials);
        when(materialMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn(page);

        // When
        List<Material> result = materialService.getAllMaterialDividePages(1, 10);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(materialMapper).selectPage(any(Page.class), any(QueryWrapper.class));
    }

    @Test
    void testGetMaterialById_Success() {
        // Given
        when(materialMapper.selectById(1)).thenReturn(testMaterial);

        // When
        Material result = materialService.getMaterialById(1);

        // Then
        assertNotNull(result);
        assertEquals(testMaterial.getId(), result.getId());
        verify(materialMapper).selectById(1);
    }

    @Test
    void testGetMaterialById_NotValid() {
        // Given
        testMaterial.setIsvalid(false);
        when(materialMapper.selectById(1)).thenReturn(testMaterial);

        // When
        Material result = materialService.getMaterialById(1);

        // Then
        assertNull(result);
    }

    @Test
    void testIsMaterialIdExist_True() {
        // Given
        when(materialMapper.selectById(1)).thenReturn(testMaterial);

        // When
        boolean result = materialService.isMaterialIdExist(1);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsMaterialIdExist_False_Null() {
        // Given
        when(materialMapper.selectById(1)).thenReturn(null);

        // When
        boolean result = materialService.isMaterialIdExist(1);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsMaterialIdExist_False_NotValid() {
        // Given
        testMaterial.setIsvalid(false);
        when(materialMapper.selectById(1)).thenReturn(testMaterial);

        // When
        boolean result = materialService.isMaterialIdExist(1);

        // Then
        assertFalse(result);
    }

    @Test
    void testUpdateMaterial_Success() {
        // Given
        when(materialMapper.selectById(1)).thenReturn(testMaterial);
        when(userService.isUserIdExist(anyInt())).thenReturn(true);
        when(herbService.isHerbIdValid(anyInt())).thenReturn(true);
        when(materialMapper.updateById(any(Material.class))).thenReturn(1);

        // When
        boolean result = materialService.updateMaterial(testMaterial);

        // Then
        assertTrue(result);
        verify(materialMapper).updateById(any(Material.class));
    }

    @Test
    void testUpdateMaterial_MaterialNotExist() {
        // Given
        when(materialMapper.selectById(1)).thenReturn(null);

        // When
        boolean result = materialService.updateMaterial(testMaterial);

        // Then
        assertFalse(result);
        verify(materialMapper, never()).updateById(any(Material.class));
    }

    @Test
    void testDeleteMaterialById_Success() {
        // Given
        when(materialMapper.selectById(1)).thenReturn(testMaterial);
        when(materialMapper.updateById(any(Material.class))).thenReturn(1);

        // When
        boolean result = materialService.deleteMaterialById(1);

        // Then
        assertTrue(result);
        assertFalse(testMaterial.isIsvalid());
        verify(materialMapper).updateById(testMaterial);
    }

    @Test
    void testDeleteMaterialById_MaterialNotExist() {
        // Given
        when(materialMapper.selectById(1)).thenReturn(null);

        // When
        boolean result = materialService.deleteMaterialById(1);

        // Then
        assertFalse(result);
        verify(materialMapper, never()).updateById(any(Material.class));
    }

    // ==================== Content Tests ====================

    @Test
    void testAddContent_Success() {
        // Given
        when(materialMapper.selectById(1)).thenReturn(testMaterial);
        when(contentMapper.insert(any(Content.class))).thenAnswer(invocation -> {
            Content content = invocation.getArgument(0);
            content.setId(1);
            return 1;
        });

        // When
        int result = materialService.addContent(testContent);

        // Then
        assertEquals(1, result);
        verify(contentMapper).insert(any(Content.class));
        assertTrue(testContent.isIsvalid());
    }

    @Test
    void testAddContent_MaterialNotExist() {
        // Given
        when(materialMapper.selectById(1)).thenReturn(null);

        // When
        int result = materialService.addContent(testContent);

        // Then
        assertEquals(-1, result);
        verify(contentMapper, never()).insert(any(Content.class));
    }

    @Test
    void testAddContent_InvalidType() {
        // Given
        testContent.setType(99); // Invalid type
        when(materialMapper.selectById(1)).thenReturn(testMaterial);

        // When
        int result = materialService.addContent(testContent);

        // Then
        assertEquals(-1, result);
        verify(contentMapper, never()).insert(any(Content.class));
    }

    @Test
    void testAddContent_EmptyDescription() {
        // Given
        testContent.setDes("");
        when(materialMapper.selectById(1)).thenReturn(testMaterial);

        // When
        int result = materialService.addContent(testContent);

        // Then
        assertEquals(-1, result);
        verify(contentMapper, never()).insert(any(Content.class));
    }

    @Test
    void testGetContentByMaterialId_Success() {
        // Given
        List<Content> expectedContents = Arrays.asList(testContent);
        when(materialMapper.selectById(1)).thenReturn(testMaterial);
        when(contentMapper.selectList(any(QueryWrapper.class))).thenReturn(expectedContents);

        // When
        List<Content> result = materialService.getContentByMaterialId(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testContent.getId(), result.get(0).getId());
    }

    @Test
    void testGetContentByMaterialId_MaterialNotExist() {
        // Given
        when(materialMapper.selectById(1)).thenReturn(null);

        // When
        List<Content> result = materialService.getContentByMaterialId(1);

        // Then
        assertNull(result);
    }

    @Test
    void testIsContentIdExist_True() {
        // Given
        when(contentMapper.selectById(1)).thenReturn(testContent);

        // When
        boolean result = materialService.isContentIdExist(1);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsContentIdExist_False_Null() {
        // Given
        when(contentMapper.selectById(1)).thenReturn(null);

        // When
        boolean result = materialService.isContentIdExist(1);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsContentIdExist_False_NotValid() {
        // Given
        testContent.setIsvalid(false);
        when(contentMapper.selectById(1)).thenReturn(testContent);

        // When
        boolean result = materialService.isContentIdExist(1);

        // Then
        assertFalse(result);
    }

    @Test
    void testUpdateContent_Success() {
        // Given
        when(contentMapper.selectById(1)).thenReturn(testContent);
        when(materialMapper.selectById(1)).thenReturn(testMaterial);
        when(contentMapper.updateById(any(Content.class))).thenReturn(1);

        // When
        boolean result = materialService.updateContent(testContent);

        // Then
        assertTrue(result);
        verify(contentMapper).updateById(any(Content.class));
    }

    @Test
    void testUpdateContent_ContentNotExist() {
        // Given
        when(contentMapper.selectById(1)).thenReturn(null);

        // When
        boolean result = materialService.updateContent(testContent);

        // Then
        assertFalse(result);
        verify(contentMapper, never()).updateById(any(Content.class));
    }

    @Test
    void testDeleteContentById_Success() {
        // Given
        when(contentMapper.selectById(1)).thenReturn(testContent);
        when(contentMapper.updateById(any(Content.class))).thenReturn(1);

        // When
        boolean result = materialService.deleteContentById(1);

        // Then
        assertTrue(result);
        assertFalse(testContent.isIsvalid());
        verify(contentMapper).updateById(testContent);
    }

    @Test
    void testDeleteContentById_ContentNotExist() {
        // Given
        when(contentMapper.selectById(1)).thenReturn(null);

        // When
        boolean result = materialService.deleteContentById(1);

        // Then
        assertFalse(result);
        verify(contentMapper, never()).updateById(any(Content.class));
    }

    // ==================== MaterialContent Tests ====================

    @Test
    void testDeleteMaterialContentByMaterialId_Success() {
        // Given
        List<Content> contents = Arrays.asList(testContent);
        when(materialMapper.selectById(1)).thenReturn(testMaterial);
        when(contentMapper.selectList(any(QueryWrapper.class))).thenReturn(contents);
        when(contentMapper.selectById(1)).thenReturn(testContent);
        when(contentMapper.updateById(any(Content.class))).thenReturn(1);
        when(materialMapper.updateById(any(Material.class))).thenReturn(1);

        // When
        boolean result = materialService.deleteMaterialContentByMaterialId(1);

        // Then
        assertTrue(result);
        verify(contentMapper).updateById(any(Content.class));
        verify(materialMapper).updateById(any(Material.class));
    }

    @Test
    void testDeleteMaterialContentByMaterialId_MaterialNotExist() {
        // Given
        when(materialMapper.selectById(1)).thenReturn(null);

        // When
        boolean result = materialService.deleteMaterialContentByMaterialId(1);

        // Then
        assertFalse(result);
    }

    // ==================== DTO Transfer Tests ====================

    @Test
    void testTransferDTOToMaterialModel() {
        // Given
        when(herbService.getHerbIdByHerbName(anyString())).thenReturn(1);

        // When
        MaterialModel result = materialService.transferDTOToMaterialModel(testMaterialDTO, 1);

        // Then
        assertNotNull(result);
        assertNotNull(result.getMaterial());
        assertEquals("测试材料", result.getMaterial().getTitle());
        assertEquals("类型1", result.getMaterial().getType());
        assertEquals(1, result.getMaterial().getUserId());
        assertEquals(1, result.getMaterial().getHerbId());
        assertEquals(0, result.getMaterial().getCount());
        assertTrue(result.getMaterial().isIsvalid());

        assertNotNull(result.getContents());
        assertEquals(1, result.getContents().size());
        assertEquals(Content.TEXT_TYPE, result.getContents().get(0).getType());
    }

    @Test
    void testTransferUpdateDTOToMaterialModel() {
        // Given
        when(herbService.getHerbIdByHerbName(anyString())).thenReturn(1);

        // When
        MaterialModel result = materialService.transferDTOToMaterialModel(testUpdateMaterialDTO, 1);

        // Then
        assertNotNull(result);
        assertNotNull(result.getMaterial());
        assertEquals(1, result.getMaterial().getId());
        assertEquals("更新材料", result.getMaterial().getTitle());
        assertEquals("类型2", result.getMaterial().getType());

        assertNotNull(result.getContents());
        assertEquals(1, result.getContents().size());
        assertEquals(1, result.getContents().get(0).getMaterialId());
    }

    @Test
    void testTransferModelToMaterialVO() {
        // Given
        List<Content> contents = Arrays.asList(testContent);
        MaterialModel materialModel = new MaterialModel(testMaterial, contents);

        when(herbService.getHerbNameByHerbId(1)).thenReturn("中药名称");
        when(userService.getUsernameById(1)).thenReturn("用户名");

        // When
        MaterialVO result = materialService.transferModelToMaterialVO(materialModel);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("测试材料", result.getTitle());
        assertEquals("中药名称", result.getHerbName());
        assertEquals("用户名", result.getUserName());

        assertNotNull(result.getContents());
        assertEquals(1, result.getContents().size());
        ContentVO contentVO = result.getContents().get(0);
        assertEquals("text", contentVO.getType());
        assertEquals("测试描述", contentVO.getDes());
    }

    @Test
    void testTransferModelToMaterialVO_WithImageAndFileContent() {
        // Given
        Content imageContent = new Content();
        imageContent.setId(2);
        imageContent.setType(Content.IMAGE_TYPE);
        imageContent.setDes("图片内容");
        imageContent.setSortOrder(2);
        imageContent.setUrl("http://example.com/image.jpg");

        Content fileContent = new Content();
        fileContent.setId(3);
        fileContent.setType(Content.FILE_TYPE);
        fileContent.setDes("文件内容");
        fileContent.setSortOrder(3);
        fileContent.setUrl("http://example.com/file.pdf");

        List<Content> contents = Arrays.asList(testContent, imageContent, fileContent);
        MaterialModel materialModel = new MaterialModel(testMaterial, contents);

        when(herbService.getHerbNameByHerbId(1)).thenReturn("中药名称");
        when(userService.getUsernameById(1)).thenReturn("用户名");

        // When
        MaterialVO result = materialService.transferModelToMaterialVO(materialModel);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContents().size());
        assertEquals("text", result.getContents().get(0).getType());
        assertEquals("image", result.getContents().get(1).getType());
        assertEquals("file", result.getContents().get(2).getType());
    }

    // ==================== MaterialModel Tests ====================

    @Test
    void testAddMaterialModel_Success() {
        // Given
        List<Content> contents = Arrays.asList(testContent);
        MaterialModel materialModel = new MaterialModel(testMaterial, contents);

        when(userService.isUserIdExist(anyInt())).thenReturn(true);
        when(herbService.isHerbIdValid(anyInt())).thenReturn(true);
        when(materialMapper.insert(any(Material.class))).thenAnswer(invocation -> {
            Material material = invocation.getArgument(0);
            material.setId(1);
            return 1;
        });
        when(materialMapper.selectById(1)).thenReturn(testMaterial);
        when(contentMapper.insert(any(Content.class))).thenAnswer(invocation -> {
            Content content = invocation.getArgument(0);
            content.setId(1);
            return 1;
        });

        // When
        int result = materialService.addMaterialModel(materialModel);

        // Then
        assertEquals(1, result);
        verify(materialMapper).insert(any(Material.class));
        verify(contentMapper).insert(any(Content.class));
    }

    @Test
    void testAddMaterialModel_MaterialFailed() {
        // Given
        testMaterial.setTitle(null);
        List<Content> contents = Arrays.asList(testContent);
        MaterialModel materialModel = new MaterialModel(testMaterial, contents);

        // When
        int result = materialService.addMaterialModel(materialModel);

        // Then
        assertEquals(-1, result);
        verify(materialMapper, never()).insert(any(Material.class));
        verify(contentMapper, never()).insert(any(Content.class));
    }

    @Test
    void testAddMaterialModel_ContentFailed() {
        // Given
        testContent.setDes(null);
        List<Content> contents = Arrays.asList(testContent);
        MaterialModel materialModel = new MaterialModel(testMaterial, contents);

        when(userService.isUserIdExist(anyInt())).thenReturn(true);
        when(herbService.isHerbIdValid(anyInt())).thenReturn(true);
        when(materialMapper.insert(any(Material.class))).thenAnswer(invocation -> {
            Material material = invocation.getArgument(0);
            material.setId(1);
            return 1;
        });
        when(materialMapper.selectById(1)).thenReturn(testMaterial);

        // When
        int result = materialService.addMaterialModel(materialModel);

        // Then
        assertEquals(-2, result);
    }

    @Test
    void testUpdateMaterialModel_Success() {
        // Given
        List<Content> oldContents = Arrays.asList(testContent);
        Content newContent = new Content();
        newContent.setMaterialId(1);
        newContent.setType(Content.TEXT_TYPE);
        newContent.setDes("新内容");
        newContent.setSortOrder(1);
        List<Content> newContents = Arrays.asList(newContent);

        MaterialModel materialModel = new MaterialModel(testMaterial, newContents);

        when(materialMapper.selectById(1)).thenReturn(testMaterial);
        when(materialMapper.updateById(any(Material.class))).thenReturn(1);
        when(contentMapper.selectList(any(QueryWrapper.class))).thenReturn(oldContents);
        when(contentMapper.selectById(1)).thenReturn(testContent);
        when(contentMapper.updateById(any(Content.class))).thenReturn(1);
        when(contentMapper.insert(any(Content.class))).thenReturn(1);

        // When
        boolean result = materialService.updateMaterialModel(materialModel);

        // Then
        assertTrue(result);
        verify(materialMapper).updateById(any(Material.class));
        verify(contentMapper).updateById(any(Content.class)); // Delete old content
        verify(contentMapper).insert(any(Content.class)); // Add new content
    }

    // ==================== SimpleMaterialVO Tests ====================

    @Test
    void testTransferMaterialToSimpleVO() {
        // Given
        when(userService.getUsernameById(1)).thenReturn("用户名");
        when(herbService.getHerbNameByHerbId(1)).thenReturn("中药名称");

        // When
        SimpleMaterialVO result = materialService.transferMaterialToSimpleVO(testMaterial);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("测试材料", result.getTitle());
        assertEquals("类型1", result.getType());
        assertEquals(1, result.getUserId());
        assertEquals(1, result.getHerbId());
        assertEquals("用户名", result.getUsername());
        assertEquals("中药名称", result.getHerbName());
    }

    @Test
    void testTransferMaterialToSimpleVOList() {
        // Given
        List<Material> materialList = Arrays.asList(testMaterial, testMaterial);
        when(userService.getUsernameById(1)).thenReturn("用户名");
        when(herbService.getHerbNameByHerbId(1)).thenReturn("中药名称");

        // When
        List<SimpleMaterialVO> result = materialService.transferMaterialToSimpleVOList(materialList);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("测试材料", result.get(0).getTitle());
        assertEquals("用户名", result.get(0).getUsername());
    }

    @Test
    void testTransferMaterialToSimpleVOList_EmptyList() {
        // Given
        List<Material> emptyList = new ArrayList<>();

        // When
        List<SimpleMaterialVO> result = materialService.transferMaterialToSimpleVOList(emptyList);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
