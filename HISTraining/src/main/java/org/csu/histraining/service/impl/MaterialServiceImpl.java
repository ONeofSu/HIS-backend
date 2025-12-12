package org.csu.histraining.service.impl;

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
import org.csu.histraining.service.MaterialService;
import org.csu.histraining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {
    @Autowired
    UserService userService;
    @Autowired
    HerbService herbService;
    @Autowired
    MaterialMapper materialMapper;
    @Autowired
    ContentMapper contentMapper;

    //----------------------------------------------------Material-------------------------------------------

    //0合法 -1标题为空 -2类型为空 -3用户不存在 -4中药不存在
    private int isInputMaterialValid(Material material) {
        if(material.getTitle()==null || material.getTitle().equals("")){
            return -1;
        }
        if(material.getType()==null || material.getType().equals("")){
            return -2;
        }
        if(!userService.isUserIdExist(material.getUserId())){
            return -3;
        }
        if(!herbService.isHerbIdValid(material.getHerbId())){
            return -4;
        }

        return 0;
    }

    //-5材料不存在 -6种类不对 -7描述为空 0正常
    private int isInputContentValid(Content content) {
        if(!isMaterialIdExist(content.getMaterialId())){
            System.out.println("isInputContentValid"+"-5");
            return -5;
        }
        if(content.getType()!=0 && content.getType()!=1 && content.getType()!=2){
            System.out.println("isInputContentValid"+"-6");
            return -6;
        }
        if(content.getDes() == null || content.getDes().equals("")){
            System.out.println("isInputContentValid"+"-7");
            return -7;
        }
        return 0;
    }

    @Override
    public int addMaterial(Material material) {
        if(isInputMaterialValid(material)!=0){
            return -1;
        }

        material.setCount(0);
        material.setIsvalid(true);
        materialMapper.insert(material);
        return material.getId();
    }

    @Override
    public List<Material> getAllMaterial() {
        QueryWrapper<Material> queryWrapper = new QueryWrapper();
        queryWrapper.eq("material_isvalid",1);
        return materialMapper.selectList(queryWrapper);
    }

    @Override
    public List<Material> getAllMaterialDividePages(int page, int size) {
        Page<Material> pageParma = new Page<>(page, size);
        QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("material_isvalid",1);
        return materialMapper.selectPage(pageParma,queryWrapper).getRecords();
    }

    @Override
    public Material getMaterialById(int id) {
        Material material = materialMapper.selectById(id);
        if(!material.isIsvalid()){
            return null;
        }
        return material;
    }

    @Override
    public boolean isMaterialIdExist(int id) {
        Material material = materialMapper.selectById(id);
        if(material==null){
            return false;
        }
        if(!material.isIsvalid()){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateMaterial(Material material) {
        if(!isMaterialIdExist(material.getId())){
            return false;
        }
        if(isInputMaterialValid(material)!=0){
            return false;
        }
        material.setIsvalid(true);
        materialMapper.updateById(material);
        return true;
    }

    @Override
    public boolean deleteMaterialById(int id) {
        if(!isMaterialIdExist(id)){
            return false;
        }
        Material material = materialMapper.selectById(id);
        material.setIsvalid(false);
        materialMapper.updateById(material);
        return true;
    }

    //-------------------------------------------------Content-----------------------------------------------------

    @Override
    public int addContent(Content content) {
        if(isInputContentValid(content)!=0){
            return -1;
        }
        content.setIsvalid(true);
        contentMapper.insert(content);
        return content.getId();
    }

    @Override
    public List<Content> getContentByMaterialId(int materialId) {
        if(!isMaterialIdExist(materialId)){
            return null;
        }
        QueryWrapper<Content> queryWrapper = new QueryWrapper();
        queryWrapper.eq("material_id", materialId).eq("content_isvalid",1).orderByAsc("content_order");
        return contentMapper.selectList(queryWrapper);
    }

    @Override
    public boolean isContentIdExist(int contentId) {
        Content content = contentMapper.selectById(contentId);
        if(content==null){
            return false;
        }
        if(!content.isIsvalid()){
            return false;
        }
        return true;
    }

    @Override
    public boolean updateContent(Content content) {
        if(!isContentIdExist(content.getId())){
            return false;
        }
        if(!isMaterialIdExist(content.getMaterialId())){
            return false;
        }
        if(isInputContentValid(content)!=0){
            return false;
        }
        content.setIsvalid(true);
        contentMapper.updateById(content);
        return true;
    }

    @Override
    public boolean deleteContentById(int id) {
        if(!isContentIdExist(id)){
            return false;
        }
        System.out.println("deleteContentById");
        Content content = contentMapper.selectById(id);
        content.setIsvalid(false);
        contentMapper.updateById(content);
        return true;
    }

    //---------------------------------------------MaterialContent------------------------------------------------------
    @Override
    public boolean deleteMaterialContentByMaterialId(int materialId) {
        if(!isMaterialIdExist(materialId)){
            return false;
        }

        List<Content> contents = getContentByMaterialId(materialId);
        for(Content content : contents){
            deleteContentById(content.getId());
        }
        deleteMaterialById(materialId);

        return true;
    }

    @Override
    public MaterialModel transferDTOToMaterialModel(MaterialDTO materialDTO,int userId) {
        Material material = new Material();
        List<Content> contents = new ArrayList<>();

        //处理Material
        material.setTitle(materialDTO.getTitle());
        material.setType(materialDTO.getType());
        material.setHerbId(herbService.getHerbIdByHerbName(materialDTO.getHerbName()));
        material.setUserId(userId);
        material.setTime(new Timestamp(System.currentTimeMillis()));
        material.setCount(0);
        material.setIsvalid(true);
        if(materialDTO.getDes() != null && !materialDTO.getDes().equals("")){
            material.setDes(materialDTO.getDes());
        }

        //处理Content
        for(ContentDTO contentDTO: materialDTO.getContents()){
            Content newContent = new Content();
            newContent.setSortOrder(contentDTO.getOrder());
            newContent.setDes(contentDTO.getDes());
            newContent.setIsvalid(true);

            switch (contentDTO.getType()) {
                case "文字" -> newContent.setType(Content.TEXT_TYPE);
                case "图片" -> newContent.setType(Content.IMAGE_TYPE);
                case "文件" -> newContent.setType(Content.FILE_TYPE);
                default -> newContent.setType(Content.ERROR_TYPE);
            }

            if(contentDTO.getUrl()!=null && !contentDTO.getUrl().equals("")){
                newContent.setUrl(contentDTO.getUrl());
            }
            contents.add(newContent);
        }

        MaterialModel model = new MaterialModel(material,contents);
        return model;
    }

    @Override
    public MaterialModel transferDTOToMaterialModel(UpdateMaterialDTO materialDTO,int userId) {
        Material material = new Material();
        List<Content> contents = new ArrayList<>();

        //处理Material
        material.setId(materialDTO.getId());
        material.setTitle(materialDTO.getTitle());
        material.setType(materialDTO.getType());
        material.setHerbId(herbService.getHerbIdByHerbName(materialDTO.getHerbName()));
        material.setUserId(userId);
        material.setTime(new Timestamp(System.currentTimeMillis()));
        material.setCount(0);
        material.setIsvalid(true);
        if(materialDTO.getDes() != null && !materialDTO.getDes().equals("")){
            material.setDes(materialDTO.getDes());
        }

        //处理Content
        for(ContentDTO contentDTO: materialDTO.getContents()){
            Content newContent = new Content();
            newContent.setMaterialId(materialDTO.getId());
            newContent.setSortOrder(contentDTO.getOrder());
            newContent.setDes(contentDTO.getDes());
            newContent.setIsvalid(true);

            switch (contentDTO.getType()) {
                case "文字" -> newContent.setType(Content.TEXT_TYPE);
                case "图片" -> newContent.setType(Content.IMAGE_TYPE);
                case "文件" -> newContent.setType(Content.FILE_TYPE);
                default -> newContent.setType(Content.ERROR_TYPE);
            }

            if(contentDTO.getUrl()!=null && !contentDTO.getUrl().equals("")){
                newContent.setUrl(contentDTO.getUrl());
            }
            contents.add(newContent);
        }

        MaterialModel model = new MaterialModel(material,contents);
        return model;
    }

    @Override
    public MaterialVO transferModelToMaterialVO(MaterialModel materialModel) {
        MaterialVO materialVO = new MaterialVO();
        materialVO.setId(materialModel.getMaterial().getId());
        materialVO.setTitle(materialModel.getMaterial().getTitle());
        materialVO.setType(materialModel.getMaterial().getType());
        materialVO.setHerbId(materialModel.getMaterial().getHerbId());
        materialVO.setUserId(materialModel.getMaterial().getUserId());
        materialVO.setTime(materialModel.getMaterial().getTime());
        materialVO.setCount(materialModel.getMaterial().getCount());

        if(materialModel.getMaterial().getDes() != null && !materialModel.getMaterial().getDes().equals("")){
            materialVO.setDes(materialModel.getMaterial().getDes());
        }
        materialVO.setHerbName(herbService.getHerbNameByHerbId(materialVO.getHerbId()));
        materialVO.setUserName(userService.getUsernameById(materialVO.getUserId()));

        List<ContentVO> contentVOS = new ArrayList<>();
        for (int i=0;i<materialModel.getContents().size();i++){
            ContentVO contentVO = new ContentVO();
            contentVO.setId(materialModel.getContents().get(i).getId());
            contentVO.setOrder(materialModel.getContents().get(i).getSortOrder());
            contentVO.setDes(materialModel.getContents().get(i).getDes());

            if(materialModel.getContents().get(i).getUrl()!=null
                    && !materialModel.getContents().get(i).getUrl().equals("")){
                contentVO.setUrl(materialModel.getContents().get(i).getUrl());
            }

            switch (materialModel.getContents().get(i).getType()) {
                case Content.TEXT_TYPE -> contentVO.setType("text");
                case Content.IMAGE_TYPE -> contentVO.setType("image");
                case Content.FILE_TYPE -> contentVO.setType("file");
                default -> contentVO.setType("error");
            }
            contentVOS.add(contentVO);
        }

        materialVO.setContents(contentVOS);

        return materialVO;
    }

    @Override
    public int addMaterialModel(MaterialModel materialModel) {
        int flag;
        flag = addMaterial(materialModel.getMaterial());
        if(flag==-1){
            return -1;
        }

        for (int i=0;i<materialModel.getContents().size();i++){
            Content content = materialModel.getContents().get(i);
            content.setMaterialId(flag);
            System.out.println("addMaterialModel-flag:"+flag);
            int result = addContent(content);
            if(result == -1){
                return -2;
            }
            materialModel.getContents().get(i).setId(result);
        }

        return flag;
    }

    @Override
    public boolean updateMaterialModel(MaterialModel materialModel) {
        Material material = getMaterialById(materialModel.getMaterial().getId());
        material.setTitle(materialModel.getMaterial().getTitle());
        material.setType(materialModel.getMaterial().getType());
        material.setHerbId(materialModel.getMaterial().getHerbId());
        material.setUserId(materialModel.getMaterial().getUserId());
        material.setTime(materialModel.getMaterial().getTime());
        material.setCount(materialModel.getMaterial().getCount());

        updateMaterial(material);

        List<Content> oldContents = getContentByMaterialId(materialModel.getMaterial().getId());

        for (Content oldContent : oldContents) {
            System.out.println("deleting old...");
            System.out.println(oldContent.getId());
            deleteContentById(oldContent.getId());
        }

        for (int i=0;i<materialModel.getContents().size();i++){
            Content content = materialModel.getContents().get(i);
            content.setMaterialId(materialModel.getMaterial().getId());
            addContent(content);
        }

        return true;
    }

    @Override
    public SimpleMaterialVO transferMaterialToSimpleVO(Material material) {
        SimpleMaterialVO simpleMaterialVO = new SimpleMaterialVO();
        simpleMaterialVO.setId(material.getId());
        simpleMaterialVO.setTitle(material.getTitle());
        simpleMaterialVO.setType(material.getType());
        simpleMaterialVO.setHerbId(material.getHerbId());
        simpleMaterialVO.setUserId(material.getUserId());
        simpleMaterialVO.setTime(material.getTime());
        simpleMaterialVO.setCount(material.getCount());

        simpleMaterialVO.setUsername(userService.getUsernameById(material.getUserId()));
        simpleMaterialVO.setHerbName(herbService.getHerbNameByHerbId(material.getHerbId()));

        if(material.getDes() != null && !material.getDes().equals("")){
            simpleMaterialVO.setDes(material.getDes());
        }
        return simpleMaterialVO;
    }

    @Override
    public List<SimpleMaterialVO> transferMaterialToSimpleVOList(List<Material> materialList) {
        List<SimpleMaterialVO> simpleMaterialVOList = new ArrayList<>();
        for (Material material : materialList) {
            SimpleMaterialVO simpleMaterialVO = transferMaterialToSimpleVO(material);
            simpleMaterialVOList.add(simpleMaterialVO);
        }
        return simpleMaterialVOList;
    }
}
