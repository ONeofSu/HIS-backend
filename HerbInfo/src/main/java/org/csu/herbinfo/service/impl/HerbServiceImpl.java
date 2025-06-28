package org.csu.herbinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.herbinfo.entity.Herb;
import org.csu.herbinfo.entity.HerbCategory;
import org.csu.herbinfo.entity.HerbLinkCategory;
import org.csu.herbinfo.mapper.HerbCategoryMapper;
import org.csu.herbinfo.mapper.HerbLinkCategoryMapper;
import org.csu.herbinfo.mapper.HerbMapper;
import org.csu.herbinfo.service.HerbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HerbServiceImpl implements HerbService {
    @Autowired
    HerbMapper herbMapper;
    @Autowired
    HerbCategoryMapper herbCategoryMapper;
    @Autowired
    HerbLinkCategoryMapper herbLinkCategoryMapper;

    //------------------------------------------HERB-------------------------------------------------------
    @Override
    public List<Herb> getAllHerbs() {
        ArrayList<Herb> herbs = new ArrayList<>();
        QueryWrapper<Herb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("herb_isvalid",1);
        herbs.addAll(herbMapper.selectList(queryWrapper));
        return herbs;
    }

    @Override
    public Herb getHerbById(int id) {
        Herb herb = null;
        herb = herbMapper.selectById(id);
        if (herb == null || !herb.isIsvalid()) {
            return null;
        }
        return herb;
    }

    @Override
    public boolean addHerb(Herb herb) {
        if (herb.getName() == null || herb.getName().equals("")) {
            return false;
        }
        herb.setIsvalid(true);
        herbMapper.insert(herb);
        return true;
    }

    @Override
    public Herb getHerbByName(String name) {
        Herb herb = null;
        QueryWrapper<Herb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("herb_name", name);
        herb = herbMapper.selectOne(queryWrapper);
        if (herb == null || !herb.isIsvalid()) {
            return null;
        }
        return herb;
    }

    @Override
    public int getHerbIdByName(String name) {
        Herb herb = getHerbByName(name);
        if (herb == null) {
            return -1;
        }
        return herb.getId();
    }

    @Override
    public boolean updateHerbForId(Herb herb) {
        Herb ori_herb = getHerbById(herb.getId());
        if (ori_herb == null || !ori_herb.isIsvalid()) {
            return false;
        }
        if (herb.getName() == null || herb.getName().equals("")) {
            return false;
        }
        herb.setIsvalid(true);
        herbMapper.updateById(herb);
        return true;
    }

    @Override
    public boolean deleteHerbById(int id) {
        if(isExistLinkOnHerb(id)){
            return false;
        }
        Herb herb = getHerbById(id);
        if (herb == null) {
            return false;
        }
        herb.setIsvalid(false);
        herbMapper.updateById(herb);
        return true;
    }

    @Override
    public boolean isHerbIdExist(int id) {
        Herb herb = getHerbById(id);
        if (herb == null || !herb.isIsvalid()) {
            return false;
        }
        return true;
    }

    //------------------------------------------HERB_CATEGORY-----------------------------------------------
    @Override
    public List<HerbCategory> getAllHerbCategories() {
        ArrayList<HerbCategory> herbCategories = new ArrayList<>();
        herbCategories.addAll(herbCategoryMapper.selectList(null));
        return herbCategories;
    }

    @Override
    public HerbCategory getHerbCategoryById(int id) {
        HerbCategory herbCategory = herbCategoryMapper.selectById(id);
        if (herbCategory == null || herbCategory.getName()==null) {
            return null;
        }
        return herbCategory;
    }

    @Override
    public int getHerbCategoryIdByName(String name) {
        if (name == null || name.equals("")) {
            return -1;
        }
        QueryWrapper<HerbCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_name", name);
        HerbCategory herbCategory = herbCategoryMapper.selectOne(queryWrapper);
        if (herbCategory == null) {
            return -1;
        }
        return herbCategory.getId();
    }

    @Override
    public boolean addHerbCategory(HerbCategory herbCategory) {
        if (herbCategory.getName() == null || herbCategory.getName().equals("")) {
            return false;
        }
        herbCategoryMapper.insert(herbCategory);
        return true;
    }

    @Override
    public boolean deleteHerbCategoryById(int id) {
        if(isExistLinkOnCategory(id)){
            return false;   //存在映射 不能删除
        }
        HerbCategory herbCategory = getHerbCategoryById(id);
        if (herbCategory == null) {
            return false;
        }
        herbCategoryMapper.deleteById(id);
        return true;
    }

    //----------------------------HERB_LINK_CATEGORY----------------------------------------------
    @Override
    public boolean addHerbLinkCategory(int herbId, int categoryId) {
        if(getHerbById(herbId)==null || getHerbCategoryById(categoryId)==null) {
            return false;
        }
        HerbLinkCategory herbLinkCategory = new HerbLinkCategory();
        herbLinkCategory.setHerbId(herbId);
        herbLinkCategory.setCategoryId(categoryId);
        QueryWrapper<HerbLinkCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("herb_id", herbId).eq("category_id", categoryId);
        if(herbLinkCategoryMapper.selectOne(queryWrapper)!=null) {
            return false;   //已经存在关系映射
        }
        herbLinkCategoryMapper.insert(herbLinkCategory);
        return true;
    }

    @Override
    public boolean deleteHerbLinkCategory(int herbId, int categoryId) {
        QueryWrapper<HerbLinkCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("herb_id", herbId).eq("category_id", categoryId);
        herbLinkCategoryMapper.delete(queryWrapper);
        return true;
    }

    @Override
    public List<HerbLinkCategory> getLinksOnHerb(int herbId) {
        ArrayList<HerbLinkCategory> list = new ArrayList<HerbLinkCategory>();
        QueryWrapper<HerbLinkCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("herb_id", herbId);
        list.addAll(herbLinkCategoryMapper.selectList(queryWrapper));
        return list;
    }

    @Override
    public List<HerbLinkCategory> getLinksOnHerbCategory(int herbCategoryId) {
        ArrayList<HerbLinkCategory> list = new ArrayList<>();
        QueryWrapper<HerbLinkCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", herbCategoryId);
        list.addAll(herbLinkCategoryMapper.selectList(queryWrapper));
        return list;
    }

    @Override
    public boolean isExistLinkOnHerb(int herbId) {
        QueryWrapper<HerbLinkCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("herb_id", herbId);
        HerbLinkCategory herbLinkCategory = herbLinkCategoryMapper.selectOne(queryWrapper);
        if (herbLinkCategory == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isExistLinkOnCategory(int categoryId) {
        QueryWrapper<HerbLinkCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        HerbLinkCategory herbLinkCategory = herbLinkCategoryMapper.selectOne(queryWrapper);
        if (herbLinkCategory == null) {
            return false;
        }
        return true;
    }
}
