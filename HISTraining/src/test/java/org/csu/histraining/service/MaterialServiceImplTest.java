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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialServiceImplTest {

    @Mock
    private HerbService herbService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MaterialServiceImpl materialService;  // 里面有 transferModelToMaterialVO()

    // ---------- 工具方法 ----------

    private Material createBaseMaterial(String des, int herbId, int userId) {
        Material m = new Material();
        m.setId(1);
        m.setTitle("T1");
        m.setType("A");
        m.setDes(des);
        m.setHerbId(herbId);
        m.setUserId(userId);
        m.setTime(Timestamp.valueOf("2024-01-01 10:00:00"));
        m.setCount(5);
        m.setIsvalid(true);
        return m;
    }

    private Content createContent(int id, int type, int order, String des, String url) {
        Content c = new Content();
        c.setId(id);
        c.setType(type);
        c.setSortOrder(order);
        c.setDes(des);
        c.setUrl(url);
        c.setMaterialId(1);
        c.setIsvalid(true);
        return c;
    }

    // ========== TC01 正常情况 ==========

    @Test
    void testTransfer_normal_withText_noUrl() {
        Material material = createBaseMaterial("描述1", 10, 100);
        Content c1 = createContent(11, Content.TEXT_TYPE, 1, "c1", null);
        MaterialModel model = new MaterialModel(material, List.of(c1));

        when(herbService.getHerbNameByHerbId(10)).thenReturn("H10");
        when(userService.getUsernameById(100)).thenReturn("U100");

        MaterialVO vo = materialService.transferModelToMaterialVO(model);

        assertEquals(1, vo.getId());
        assertEquals("T1", vo.getTitle());
        assertEquals("A", vo.getType());
        assertEquals(10, vo.getHerbId());
        assertEquals(100, vo.getUserId());
        assertEquals(5, vo.getCount());
        assertEquals("描述1", vo.getDes());
        assertEquals("H10", vo.getHerbName());
        assertEquals("U100", vo.getUserName());

        assertEquals(1, vo.getContents().size());
        ContentVO cvo = vo.getContents().get(0);
        assertEquals(11, cvo.getId());
        assertEquals(1, cvo.getOrder());
        assertEquals("c1", cvo.getDes());
        assertNull(cvo.getUrl());
        assertEquals("text", cvo.getType());
    }

    // ========== TC02 des 为 null ==========

    @Test
    void testTransfer_desIsNull() {
        Material material = createBaseMaterial(null, 10, 100);
        Content c1 = createContent(11, Content.TEXT_TYPE, 1, "c1", null);
        MaterialModel model = new MaterialModel(material, List.of(c1));

        when(herbService.getHerbNameByHerbId(10)).thenReturn("H10");
        when(userService.getUsernameById(100)).thenReturn("U100");

        MaterialVO vo = materialService.transferModelToMaterialVO(model);

        assertNull(vo.getDes());
    }

    // ========== TC03 des 为空字符串 ==========

    @Test
    void testTransfer_desIsEmptyString() {
        Material material = createBaseMaterial("", 10, 100);
        Content c1 = createContent(11, Content.TEXT_TYPE, 1, "c1", null);
        MaterialModel model = new MaterialModel(material, List.of(c1));

        when(herbService.getHerbNameByHerbId(10)).thenReturn("H10");
        when(userService.getUsernameById(100)).thenReturn("U100");

        MaterialVO vo = materialService.transferModelToMaterialVO(model);

        assertNull(vo.getDes());
    }

    // ========== TC04 contents 为空列表 ==========

    @Test
    void testTransfer_contentsEmptyList() {
        Material material = createBaseMaterial("描述1", 10, 100);
        MaterialModel model = new MaterialModel(material, Collections.emptyList());

        when(herbService.getHerbNameByHerbId(10)).thenReturn("H10");
        when(userService.getUsernameById(100)).thenReturn("U100");

        MaterialVO vo = materialService.transferModelToMaterialVO(model);

        assertNotNull(vo.getContents());
        assertEquals(0, vo.getContents().size());
    }

    // ========== TC05 url 非空 ==========

    @Test
    void testTransfer_urlNotEmpty() {
        Material material = createBaseMaterial("描述1", 10, 100);
        Content c1 = createContent(12, Content.IMAGE_TYPE, 2, "img", "http://x/1.png");
        MaterialModel model = new MaterialModel(material, List.of(c1));

        when(herbService.getHerbNameByHerbId(10)).thenReturn("H10");
        when(userService.getUsernameById(100)).thenReturn("U100");

        MaterialVO vo = materialService.transferModelToMaterialVO(model);

        assertEquals(1, vo.getContents().size());
        ContentVO cvo = vo.getContents().get(0);
        assertEquals("http://x/1.png", cvo.getUrl());
        assertEquals("image", cvo.getType());
    }

    // ========== TC06 url 为空字符串 ==========

    @Test
    void testTransfer_urlEmptyString() {
        Material material = createBaseMaterial("描述1", 10, 100);
        Content c1 = createContent(13, Content.FILE_TYPE, 3, "file", "");
        MaterialModel model = new MaterialModel(material, List.of(c1));

        when(herbService.getHerbNameByHerbId(10)).thenReturn("H10");
        when(userService.getUsernameById(100)).thenReturn("U100");

        MaterialVO vo = materialService.transferModelToMaterialVO(model);

        ContentVO cvo = vo.getContents().get(0);
        assertNull(cvo.getUrl());
        assertEquals("file", cvo.getType());
    }

    // ========== TC07 多条内容 + 三种合法 type ==========

    @Test
    void testTransfer_multiContents_allTypes() {
        Material material = createBaseMaterial("描述1", 10, 100);
        Content cText  = createContent(21, Content.TEXT_TYPE, 1, "text", null);
        Content cImage = createContent(22, Content.IMAGE_TYPE, 2, "img", "http://x/img.png");
        Content cFile  = createContent(23, Content.FILE_TYPE, 3, "file", "http://x/file.pdf");

        MaterialModel model = new MaterialModel(material, List.of(cText, cImage, cFile));

        when(herbService.getHerbNameByHerbId(10)).thenReturn("H10");
        when(userService.getUsernameById(100)).thenReturn("U100");

        MaterialVO vo = materialService.transferModelToMaterialVO(model);

        assertEquals(3, vo.getContents().size());
        assertEquals("text",  vo.getContents().get(0).getType());
        assertEquals("image", vo.getContents().get(1).getType());
        assertEquals("file",  vo.getContents().get(2).getType());
    }

    // ========== TC08 非法 type（default 分支） ==========

    @Test
    void testTransfer_invalidType_toError() {
        Material material = createBaseMaterial("描述1", 10, 100);
        Content cErr = createContent(30, 999, 1, "err", null);
        MaterialModel model = new MaterialModel(material, List.of(cErr));

        when(herbService.getHerbNameByHerbId(10)).thenReturn("H10");
        when(userService.getUsernameById(100)).thenReturn("U100");

        MaterialVO vo = materialService.transferModelToMaterialVO(model);

        assertEquals(1, vo.getContents().size());
        assertEquals("error", vo.getContents().get(0).getType());
    }

    // ========== TC09 确认使用 VO 中的 herbId/userId 调用服务 ==========

    @Test
    void testTransfer_herbAndUserServiceCalledWithCorrectIds() {
        Material material = createBaseMaterial("描述1", 77, 88);
        Content c1 = createContent(11, Content.TEXT_TYPE, 1, "c1", null);
        MaterialModel model = new MaterialModel(material, List.of(c1));

        when(herbService.getHerbNameByHerbId(77)).thenReturn("Herb77");
        when(userService.getUsernameById(88)).thenReturn("User88");

        MaterialVO vo = materialService.transferModelToMaterialVO(model);

        assertEquals("Herb77", vo.getHerbName());
        assertEquals("User88", vo.getUserName());

        verify(herbService, times(1)).getHerbNameByHerbId(77);
        verify(userService, times(1)).getUsernameById(88);
    }

    // ========== TC10 contents 为 null（当前实现会 NPE） ==========

    @Test
    void testTransfer_contentsNull_expectNpe() {
        Material material = createBaseMaterial("描述1", 10, 100);
        MaterialModel model = new MaterialModel(material, null);

        when(herbService.getHerbNameByHerbId(10)).thenReturn("H10");
        when(userService.getUsernameById(100)).thenReturn("U100");

        assertThrows(NullPointerException.class,
                () -> materialService.transferModelToMaterialVO(model));
    }
}
