package org.csu.histraining.service;

import org.csu.histraining.DTO.MaterialDTO;
import org.csu.histraining.DTO.UpdateMaterialDTO;
import org.csu.histraining.VO.MaterialVO;
import org.csu.histraining.VO.SimpleMaterialVO;
import org.csu.histraining.entity.Content;
import org.csu.histraining.entity.Material;
import org.csu.histraining.model.MaterialModel;

import java.util.List;

public interface MaterialService {
    public int addMaterial(Material material);
    public List<Material> getAllMaterial();
    public Material getMaterialById(int id);
    public boolean isMaterialIdExist(int id);
    public boolean updateMaterial(Material material);
    public boolean deleteMaterialById(int id);

    public int addContent(Content content);
    public List<Content> getContentByMaterialId(int materialId);
    public boolean isContentIdExist(int contentId);
    public boolean updateContent(Content content);
    public boolean deleteContentById(int id);

    public boolean deleteMaterialContentByMaterialId(int materialId);

    public MaterialModel transferDTOToMaterialModel(MaterialDTO materialDTO,int userId);
    public MaterialModel transferDTOToMaterialModel(UpdateMaterialDTO updateMaterialDTO,int userId);
    public MaterialVO transferModelToMaterialVO(MaterialModel materialModel);
    public SimpleMaterialVO transferMaterialToSimpleVO(Material material);
    public List<SimpleMaterialVO> transferMaterialToSimpleVOList(List<Material> materialList);

    public int addMaterialModel(MaterialModel materialModel);
    public boolean updateMaterialModel(MaterialModel materialModel);
}
