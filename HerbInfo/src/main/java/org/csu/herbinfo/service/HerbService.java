package org.csu.herbinfo.service;

import org.csu.herbinfo.DTO.HerbDTO;
import org.csu.herbinfo.entity.Herb;
import org.csu.herbinfo.entity.HerbCategory;
import org.csu.herbinfo.entity.HerbLinkCategory;
import org.springframework.stereotype.Service;
import java.util.List;

public interface HerbService {
    public List<Herb> getAllHerbs();
    public Herb getHerbById(int id);    //空就返回NULL
    public Herb getHerbByName(String name); //空就返回NULL
    public int getHerbIdByName(String herbName);    //空返回-1
    public boolean addHerb(Herb herb);
    public boolean updateHerbForId(Herb herb);  //在对应位置上修改id
    public boolean deleteHerbById(int id);  //只在形式上删除

    public boolean isHerbIdExist(int id);
    public boolean isHerbNameExist(String name);

    public List<HerbCategory> getAllHerbCategories();
    public HerbCategory getHerbCategoryById(int id);
    public int getHerbCategoryIdByName(String herbName);
    public boolean addHerbCategory(HerbCategory herbCategory);
    public boolean deleteHerbCategoryById(int id);

    public boolean isHerbCategoryIdExist(int id);
    public boolean isHerbCategoryNameExist(String name);

    public boolean addHerbLinkCategory(int herbId, int categoryId); //建立连接映射
    public boolean deleteHerbLinkCategory(int herbId, int categoryId);  //删除连接映射
    public List<HerbLinkCategory> getLinksOnHerb(int herbId);   //获得herb上的所有映射
    public List<HerbLinkCategory> getLinksOnHerbCategory(int herbCategoryId);   //获得种类上的所有映射
    public boolean isExistLinkOnHerb(int herbId);   //检测中药是否有链接映射
    public boolean isExistLinkOnCategory(int categoryId);   //检测类别是否有链接映射

    public boolean isLinkExist(int herbId, int categoryId);

    public Herb transferDTOToHerb(HerbDTO herbDTO);
}
