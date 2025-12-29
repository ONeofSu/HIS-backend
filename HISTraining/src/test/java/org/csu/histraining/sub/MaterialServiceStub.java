package org.csu.histraining.sub;

import org.csu.histraining.DTO.MaterialDTO;
import org.csu.histraining.DTO.UpdateMaterialDTO;
import org.csu.histraining.VO.MaterialVO;
import org.csu.histraining.VO.SimpleMaterialVO;
import org.csu.histraining.entity.Content;
import org.csu.histraining.entity.Material;
import org.csu.histraining.model.MaterialModel;
import org.csu.histraining.service.MaterialService;
import java.util.*;

/**
 * MaterialService 的桩实现
 * 模拟教材服务，返回预定义的测试数据
 */
public class MaterialServiceStub implements MaterialService {

    // 模拟存在的教材 ID 集合
    private final Set<Integer> validMaterialIds = new HashSet<>(Arrays.asList(1, 2, 3));

    @Override
    public boolean isMaterialIdExist(int id) {
        return validMaterialIds.contains(id);
    }

    @Override
    public Material getMaterialById(int id) {
        if (!validMaterialIds.contains(id)) {
            return null;
        }
        Material material = new Material();
        material.setId(id);
        material.setTitle("测试教材-" + id);
        material.setType("视频");
        return material;
    }

    // 其他未使用的方法返回默认值或抛出异常
    @Override
    public int addMaterial(Material material) {
        throw new UnsupportedOperationException("Not needed in stub");
    }

    @Override
    public List<Material> getAllMaterial() {
        throw new UnsupportedOperationException("Not needed in stub");
    }

    @Override
    public List<Material> getAllMaterialDividePages(int page, int size) {
        throw new UnsupportedOperationException("Not needed in stub");
    }

    @Override
    public boolean updateMaterial(Material material) {
        throw new UnsupportedOperationException("Not needed in stub");
    }

    @Override
    public boolean deleteMaterialById(int id) {
        throw new UnsupportedOperationException("Not needed in stub");
    }

    @Override
    public int addContent(Content content) {
        return 0;
    }

    @Override
    public List<Content> getContentByMaterialId(int materialId) {
        return List.of();
    }

    @Override
    public boolean isContentIdExist(int contentId) {
        return false;
    }

    @Override
    public boolean updateContent(Content content) {
        return false;
    }

    @Override
    public boolean deleteContentById(int id) {
        return false;
    }

    @Override
    public boolean deleteMaterialContentByMaterialId(int materialId) {
        return false;
    }

    @Override
    public MaterialModel transferDTOToMaterialModel(MaterialDTO materialDTO, int userId) {
        return null;
    }

    @Override
    public MaterialModel transferDTOToMaterialModel(UpdateMaterialDTO updateMaterialDTO, int userId) {
        return null;
    }

    @Override
    public MaterialVO transferModelToMaterialVO(MaterialModel materialModel) {
        return null;
    }

    @Override
    public SimpleMaterialVO transferMaterialToSimpleVO(Material material) {
        return null;
    }

    @Override
    public List<SimpleMaterialVO> transferMaterialToSimpleVOList(List<Material> materialList) {
        return List.of();
    }

    @Override
    public int addMaterialModel(MaterialModel materialModel) {
        return 0;
    }

    @Override
    public boolean updateMaterialModel(MaterialModel materialModel) {
        return false;
    }

    // 省略其他不相关方法...
}
