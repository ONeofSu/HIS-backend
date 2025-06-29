package org.csu.herbinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.herbinfo.VO.StreetVO;
import org.csu.herbinfo.entity.District;
import org.csu.herbinfo.entity.Street;
import org.csu.herbinfo.mapper.DistrictMapper;
import org.csu.herbinfo.mapper.StreetMapper;
import org.csu.herbinfo.service.DistrictStreetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DistrictStreetServiceImpl implements DistrictStreetService {
    @Autowired
    DistrictMapper districtMapper;
    @Autowired
    StreetMapper streetMapper;

    @Override
    public int getDistrictIdByName(String districtName) {
        if(districtName == null || districtName.equals("")){
            return -1;
        }
        QueryWrapper<District> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("district_name", districtName);
        District district = districtMapper.selectOne(queryWrapper);
        if(district == null){
            return -1;
        }
        return district.getId();
    }

    @Override
    public int getStreetIdByName(String streetName) {
        if(streetName == null || streetName.equals("")){
            return -1;
        }
        QueryWrapper<Street> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("street_name", streetName);
        Street street = streetMapper.selectOne(queryWrapper);
        if(street == null){
            return -1;
        }
        return street.getStreetId();
    }

    @Override
    public List<District> getAllDistricts() {
        ArrayList<District> result = new ArrayList<>();
        result.addAll(districtMapper.selectList(null));
        return result;
    }

    @Override
    public List<Street> getAllStreetsInDistrictByDistrictId(int districtId) {
        ArrayList<Street> result = new ArrayList<>();
        QueryWrapper<Street> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("district_id", districtId);
        result.addAll(streetMapper.selectList(queryWrapper));
        return result;
    }

    @Override
    public List<Street> getAllStreetsInDistrictByDistrictName(String districtName) {
        int districtId = getDistrictIdByName(districtName);
        return getAllStreetsInDistrictByDistrictId(districtId);
    }

    @Override
    public Street getStreetById(int streetId) {
        streetMapper.selectById(streetId);
        return streetMapper.selectById(streetId);
    }

    @Override
    public District getDistrictById(int districtId) {
        districtMapper.selectById(districtId);
        return districtMapper.selectById(districtId);
    }

    @Override
    public boolean isDistrictExist(int districtId) {
        QueryWrapper<District> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("district_id", districtId);
        District district = districtMapper.selectOne(queryWrapper);
        if(district == null){
            return false;
        }
        return true;
    }

    @Override
    public boolean isStreetInDistrict(int districtId, int streetId) {
        if(!isDistrictExist(districtId)){
            return false;
        }
        Street street = streetMapper.selectById(streetId);
        if(street == null){
            return false;
        }
        if(street.getDistrictId() != districtId){
            return false;
        }
        return true;
    }

    @Override
    public StreetVO transferStreetToVO(Street street) {
        StreetVO streetVO = new StreetVO();
        streetVO.setDistrictId(street.getDistrictId());
        streetVO.setStreetId(street.getStreetId());
        streetVO.setStreetName(street.getName());

        streetVO.setDistrictName(getDistrictById(street.getDistrictId()).getName());
        return streetVO;
    }

    @Override
    public List<StreetVO> transferStreetToVOList(List<Street> streetList) {
        List<StreetVO> streetVOList = new ArrayList<>();
        for(Street street : streetList){
            StreetVO streetVO = transferStreetToVO(street);
            streetVOList.add(streetVO);
        }
        return streetVOList;
    }
}
