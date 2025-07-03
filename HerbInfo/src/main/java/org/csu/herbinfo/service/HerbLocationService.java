package org.csu.herbinfo.service;

import org.csu.herbinfo.DTO.HerbLocationDTO;
import org.csu.herbinfo.VO.HerbLocationVO;
import org.csu.herbinfo.entity.HerbLocation;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HerbLocationService {
    public List<HerbLocation> getAllHerbLocations();
    public HerbLocation getHerbLocationById(int id);
    public List<HerbLocation> getHerbLocationsByHerbId(int herbId);
    public List<HerbLocation> getHerbLocationsByDistrictName(String district);
    public int getHerbCountsByDistrictName(String district);
    public List<HerbLocation> getHerbLocationsByStreetName(String street);
    public boolean addHerbLocation(HerbLocation herbLocation);
    public boolean updateHerbLocation(HerbLocation herbLocation);
    public int isHerbLocationInfoExists(HerbLocation herbLocation); //若存在位置相同的返回HerbLocation id 不存在返回-1
    public HerbLocation getHerbLocationByLocationInfo(HerbLocation herbLocation);
    public boolean deleteHerbLocation(int herbLocationId);

    public boolean isHerbLocationExist(int herbLocationId);

    public HerbLocation transferDTOToHerbLocation(HerbLocationDTO herbLocationDTO);
    public HerbLocationVO transferHerbLocationToVO(HerbLocation herbLocation);
    public List<HerbLocationVO> transferHerbLocationListToVOList(List<HerbLocation> herbLocationList);
}
