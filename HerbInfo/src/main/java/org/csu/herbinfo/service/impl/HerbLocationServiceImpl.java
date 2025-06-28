package org.csu.herbinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.herbinfo.entity.HerbGrowth;
import org.csu.herbinfo.entity.HerbLocation;
import org.csu.herbinfo.mapper.HerbGrowthMapper;
import org.csu.herbinfo.mapper.HerbLocationMapper;
import org.csu.herbinfo.service.HerbLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HerbLocationServiceImpl implements HerbLocationService {
    @Autowired
    HerbLocationMapper herbLocationMapper;

    private boolean isHerbLocationValid(HerbLocation herbLocation) {
        if(herbLocation==null){
            return false;
        }
//        if(herbLocation.getDistrict()==null || herbLocation.getDistrict().equals("") ||
//        herbLocation.getStreet()==null || herbLocation.getStreet().equals("")){
//            return false;
//        }
        if(herbLocation.getCount()==0) return false;
        return true;
    }

    @Override
    public List<HerbLocation> getAllHerbLocations() {
        ArrayList<HerbLocation> list = new ArrayList<>();
        list.addAll(herbLocationMapper.selectList(null));
        return list;
    }

    @Override
    public List<HerbLocation> getHerbLocationsByHerbId(int herbId) {
        ArrayList<HerbLocation> list = new ArrayList<>();
        QueryWrapper<HerbLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("herb_id", herbId);
        list.addAll(herbLocationMapper.selectList(queryWrapper));
        return list;
    }

//    @Override
//    public List<HerbLocation> getHerbLocationsByDistrict(String district) {
//        ArrayList<HerbLocation> list = new ArrayList<>();
//        QueryWrapper<HerbLocation> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("district", district);
//        list.addAll(herbLocationMapper.selectList(queryWrapper));
//        return list;
//    }
//
//    @Override
//    public List<HerbLocation> getHerbLocationsByStreet(String street) {
//        ArrayList<HerbLocation> list = new ArrayList<>();
//        QueryWrapper<HerbLocation> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("street", street);
//        list.addAll(herbLocationMapper.selectList(queryWrapper));
//        return list;
//    }

    @Override
    public boolean addHerbLocation(HerbLocation herbLocation) {
        if(!isHerbLocationValid(herbLocation)){
            return false;
        }
        herbLocationMapper.insert(herbLocation);
        return true;
    }
}
