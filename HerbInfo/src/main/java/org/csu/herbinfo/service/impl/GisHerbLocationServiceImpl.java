package org.csu.herbinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.herbinfo.DTO.HerbLocationDTO;
import org.csu.herbinfo.VO.HerbLocationVO;
import org.csu.herbinfo.entity.GisHerbLocation;
import org.csu.herbinfo.entity.HerbLocation;
import org.csu.herbinfo.mapper.GisHerbLocationPGSqlMapper;
import org.csu.herbinfo.service.DistrictStreetService;
import org.csu.herbinfo.service.GisHerbLocationService;
import org.csu.herbinfo.service.HerbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * todo 重构类型转换方法
 */

@Service
public class GisHerbLocationServiceImpl implements GisHerbLocationService {
    @Autowired
    GisHerbLocationPGSqlMapper gisHerbLocationMapper;
    @Autowired
    DistrictStreetService districtStreetService;
    @Autowired
    HerbService herbService;

    private boolean isHerbLocationValid(GisHerbLocation herbLocation) {
        if(herbLocation==null){
            return false;
        }

        int districtId = herbLocation.getDistrictId();
        if(!districtStreetService.isDistrictExist(districtId)){
            return false;
        }

        int streetId = herbLocation.getStreetId();
        if(!districtStreetService.isStreetInDistrict(districtId, streetId)){
            return false;
        }

        if(herbLocation.getCount()==0) return false;
        return true;
    }

    @Override
    public List<GisHerbLocation> getAllHerbLocations() {
        ArrayList<GisHerbLocation> list = new ArrayList<>();
        list.addAll(gisHerbLocationMapper.selectList(null));
        return list;
    }

    @Override
    public GisHerbLocation getHerbLocationById(int id) {
        GisHerbLocation gisHerbLocation = gisHerbLocationMapper.selectById(id);
        return gisHerbLocation;
    }

    @Override
    public List<GisHerbLocation> getHerbLocationsByHerbId(int herbId) {
        ArrayList<GisHerbLocation> list = new ArrayList<>();
        QueryWrapper<GisHerbLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("herb_id", herbId);
        list.addAll(gisHerbLocationMapper.selectList(queryWrapper));
        return list;
    }

    @Override
    public List<GisHerbLocation> getHerbLocationsByDistrictName(String district) {
        ArrayList<GisHerbLocation> list = new ArrayList<>();
        int district_id = districtStreetService.getDistrictIdByName(district);
        QueryWrapper<GisHerbLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("district_id", district_id);
        list.addAll(gisHerbLocationMapper.selectList(queryWrapper));
        return list;
    }

    @Override
    public int getHerbCountsByDistrictName(String district) {
        int count = 0;
        int district_id = districtStreetService.getDistrictIdByName(district);
        QueryWrapper<GisHerbLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("district_id", district_id);
        List<GisHerbLocation> list = gisHerbLocationMapper.selectList(queryWrapper);
        for(GisHerbLocation gisHerbLocation : list){
            count+=gisHerbLocation.getCount();
        }
        return count;
    }

    @Override
    public List<GisHerbLocation> getHerbLocationsByStreetName(String street) {
        ArrayList<GisHerbLocation> list = new ArrayList<>();
        int street_id = districtStreetService.getStreetIdByName(street);
        QueryWrapper<GisHerbLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("street_id", street_id);
        list.addAll(gisHerbLocationMapper.selectList(queryWrapper));
        return list;
    }

    @Override
    public boolean addHerbLocation(GisHerbLocation herbLocation) {
        if(!isHerbLocationValid(herbLocation)){
            return false;
        }
        int ori_id = isHerbLocationInfoExists(herbLocation);
        if(ori_id!=-1){
            GisHerbLocation ori = gisHerbLocationMapper.selectById(ori_id);
            ori.setCount(ori.getCount()+herbLocation.getCount());
            gisHerbLocationMapper.updateById(ori);
            return true;
        }
        gisHerbLocationMapper.insert(herbLocation);
        return true;
    }

    @Override
    public boolean updateHerbLocation(GisHerbLocation herbLocation) {
        if(!isHerbLocationValid(herbLocation)){
            return false;
        }
        gisHerbLocationMapper.updateById(herbLocation);
        return true;
    }

    @Override
    public int isHerbLocationInfoExists(GisHerbLocation herbLocation) {
        GisHerbLocation flag;
        QueryWrapper<GisHerbLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("district_id", herbLocation.getDistrictId())
                .eq("street_id", herbLocation.getStreetId())
                .eq("geom", herbLocation.getGeom());
        flag = gisHerbLocationMapper.selectOne(queryWrapper);
        if(flag==null){
            return -1;
        }
        return Math.toIntExact(flag.getId());
    }

    @Override
    public GisHerbLocation getHerbLocationByLocationInfo(GisHerbLocation herbLocation) {
        int location_id = isHerbLocationInfoExists(herbLocation);
        if(location_id==-1){
            return null;
        }
        return gisHerbLocationMapper.selectById(location_id);
    }

    @Override
    public boolean deleteHerbLocation(int herbLocationId) {
        if(!isHerbLocationExist(herbLocationId)){
            return false;
        }
        gisHerbLocationMapper.deleteById(herbLocationId);
        return true;
    }

    @Override
    public boolean isHerbLocationExist(int herbLocationId) {
        if(gisHerbLocationMapper.selectById(herbLocationId)==null){
            return false;
        }
        return true;
    }

    @Override
    public HerbLocation transferDTOToHerbLocation(HerbLocationDTO herbLocationDTO) {
        return null;
    }

    @Override
    public HerbLocationVO transferHerbLocationToVO(HerbLocation herbLocation) {
        return null;
    }

    @Override
    public List<HerbLocationVO> transferHerbLocationListToVOList(List<HerbLocation> herbLocationList) {
        return List.of();
    }
}
