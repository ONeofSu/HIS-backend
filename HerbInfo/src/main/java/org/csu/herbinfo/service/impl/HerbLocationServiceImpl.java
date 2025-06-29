package org.csu.herbinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.herbinfo.DTO.HerbGrowthDTO;
import org.csu.herbinfo.DTO.HerbLocationDTO;
import org.csu.herbinfo.VO.HerbLocationVO;
import org.csu.herbinfo.entity.Herb;
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
    public HerbLocation getHerbLocationById(int id) {
        HerbLocation herbLocation = herbLocationMapper.selectById(id);
        return herbLocation;
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
        int ori_id = isHerbLocationInfoExists(herbLocation);
        if(ori_id!=-1){
            HerbLocation ori = herbLocationMapper.selectById(ori_id);
            ori.setCount(ori.getCount()+herbLocation.getCount());
            herbLocationMapper.updateById(ori);
            return true;
        }
        herbLocationMapper.insert(herbLocation);
        return true;
    }

    @Override
    public int isHerbLocationInfoExists(HerbLocation herbLocation) {
        HerbLocation flag;
        QueryWrapper<HerbLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("district_id", herbLocation.getDistrictId())
                .eq("street_id", herbLocation.getStreetId())
                .eq("location_longitude", herbLocation.getLongitude())
                .eq("location_latitude", herbLocation.getLatitude());
        flag = herbLocationMapper.selectOne(queryWrapper);
        if(flag==null){
            return -1;
        }
        return flag.getId();
    }

    @Override
    public HerbLocation getHerbLocationByLocationInfo(HerbLocation herbLocation) {
        System.out.println("getHerbLocationByLocationInfo:"+herbLocation);
        int location_id = isHerbLocationInfoExists(herbLocation);
        if(location_id==-1){
            return null;
        }
        System.out.println("getHerbLocationByLocationInfo:"+location_id);
        return herbLocationMapper.selectById(location_id);
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

    @Override
    public HerbLocationVO transferHerbLocationToVO(HerbLocation herbLocation) {
        HerbLocationVO herbLocationVO = new HerbLocationVO();
        herbLocationVO.setId(herbLocation.getId());
        herbLocationVO.setHerbId(herbLocation.getHerbId());
        herbLocationVO.setCount(herbLocation.getCount());
        herbLocationVO.setDistrictId(herbLocation.getDistrictId());
        herbLocationVO.setStreetId(herbLocation.getStreetId());
        herbLocationVO.setLatitude(herbLocation.getLatitude());
        herbLocationVO.setLongitude(herbLocation.getLongitude());

        herbLocationVO.setHerbName(herbService.getHerbById(herbLocation.getHerbId()).getName());
        herbLocationVO.setDistrictName(districtStreetService.getDistrictById(herbLocation.getDistrictId()).getName());
        herbLocationVO.setStreetName(districtStreetService.getStreetById(herbLocation.getStreetId()).getName());
        return herbLocationVO;
    }

    @Override
    public List<HerbLocationVO> transferHerbLocationListToVOList(List<HerbLocation> herbLocationList) {
        List<HerbLocationVO> herbLocationVOList = new ArrayList<>();
        for(HerbLocation herbLocation : herbLocationList){
            herbLocationVOList.add(transferHerbLocationToVO(herbLocation));
        }
        return herbLocationVOList;
    }
}
