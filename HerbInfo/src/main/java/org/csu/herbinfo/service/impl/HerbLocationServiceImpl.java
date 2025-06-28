package org.csu.herbinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.herbinfo.DTO.HerbGrowthDTO;
import org.csu.herbinfo.DTO.HerbLocationDTO;
import org.csu.herbinfo.entity.HerbGrowth;
import org.csu.herbinfo.entity.HerbLocation;
import org.csu.herbinfo.mapper.HerbGrowthMapper;
import org.csu.herbinfo.mapper.HerbLocationMapper;
import org.csu.herbinfo.service.DistrictStreetService;
import org.csu.herbinfo.service.HerbLocationService;
import org.csu.herbinfo.service.HerbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HerbLocationServiceImpl implements HerbLocationService {
    @Autowired
    HerbLocationMapper herbLocationMapper;
    @Autowired
    DistrictStreetService districtStreetService;
    @Autowired
    private HerbService herbService;

    private boolean isHerbLocationValid(HerbLocation herbLocation) {
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

    @Override
    public List<HerbLocation> getHerbLocationsByDistrictName(String district) {
        ArrayList<HerbLocation> list = new ArrayList<>();
        int district_id = districtStreetService.getDistrictIdByName(district);
        QueryWrapper<HerbLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("district_id", district_id);
        list.addAll(herbLocationMapper.selectList(queryWrapper));
        return list;
    }

    @Override
    public List<HerbLocation> getHerbLocationsByStreetName(String street) {
        ArrayList<HerbLocation> list = new ArrayList<>();
        int street_id = districtStreetService.getStreetIdByName(street);
        QueryWrapper<HerbLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("street_id", street_id);
        list.addAll(herbLocationMapper.selectList(queryWrapper));
        return list;
    }

    @Override
    public boolean addHerbLocation(HerbLocation herbLocation) {
        if(!isHerbLocationValid(herbLocation)){
            return false;
        }
        herbLocationMapper.insert(herbLocation);
        return true;
    }

    @Override
    public HerbLocation transferDTOToHerbLocation(HerbLocationDTO herbLocationDTO) {
        HerbLocation herbLocation = new HerbLocation();

        int herbId = herbService.getHerbIdByName(herbLocationDTO.getName());
        herbLocation.setHerbId(herbId);

        herbLocation.setCount(herbLocationDTO.getCount());

        int district_id = districtStreetService.getDistrictIdByName(herbLocationDTO.getDistrict());
        herbLocation.setDistrictId(district_id);

        int street_id = districtStreetService.getStreetIdByName(herbLocationDTO.getStreet());
        herbLocation.setStreetId(street_id);

        herbLocation.setLatitude(herbLocationDTO.getLatitude());
        herbLocation.setLongitude(herbLocationDTO.getLongitude());

        return herbLocation;
    }

    @Override
    public boolean deleteHerbLocation(int herbLocationId) {
        if(!isHerbLocationExist(herbLocationId)){
            return false;
        }
        herbLocationMapper.deleteById(herbLocationId);
        return true;
    }

    @Override
    public boolean isHerbLocationExist(int herbLocationId) {
        if(herbLocationMapper.selectById(herbLocationId)==null){
            return false;
        }
        return true;
    }
}
