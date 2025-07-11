package org.csu.herbinfo.service;

import org.csu.herbinfo.VO.StreetVO;
import org.csu.herbinfo.entity.District;
import org.csu.herbinfo.entity.Street;

import java.util.List;

public interface DistrictStreetService {
    public int getDistrictIdByName(String districtName);    //返回负一即不存在
    public int getStreetIdByName(String streetName);    //返回负一即不存在
    public List<District> getAllDistricts();
    public List<Street> getAllStreetsInDistrictByDistrictId(int districtId);
    public List<Street> getAllStreetsInDistrictByDistrictName(String districtName);
    public Street getStreetById(int streetId);
    public District getDistrictById(int districtId);

    public boolean isDistrictExist(int districtId);
    public boolean isStreetInDistrict(int districtId, int streetId);

    public StreetVO transferStreetToVO(Street street);
    public List<StreetVO> transferStreetToVOList(List<Street> streetList);
}
