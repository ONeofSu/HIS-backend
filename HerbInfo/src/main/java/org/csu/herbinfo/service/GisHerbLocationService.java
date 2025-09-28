package org.csu.herbinfo.service;

import org.csu.herbinfo.DTO.HerbLocationDTO;
import org.csu.herbinfo.DTO.Location;
import org.csu.herbinfo.VO.HerbLocationVO;
import org.csu.herbinfo.entity.GisHerbLocation;
import org.csu.herbinfo.entity.HerbLocation;

import java.util.List;

public interface GisHerbLocationService {
    List<GisHerbLocation> getAllHerbLocations();
    GisHerbLocation getHerbLocationById(int id);
    List<GisHerbLocation> getHerbLocationsByHerbId(int herbId);
    List<GisHerbLocation> getHerbLocationsByDistrictName(String district);
    int getHerbCountsByDistrictName(String district);
    List<GisHerbLocation> getHerbLocationsByStreetName(String street);
    int addHerbLocation(GisHerbLocation herbLocation);
    boolean updateHerbLocation(GisHerbLocation herbLocation);
    int isHerbLocationInfoExists(GisHerbLocation herbLocation); //若存在位置相同的返回HerbLocation id 不存在返回-1
    GisHerbLocation getHerbLocationByLocationInfo(GisHerbLocation herbLocation);
    boolean deleteHerbLocation(int herbLocationId);

    boolean isHerbLocationExist(int herbLocationId);

    /**
     * 获得500m范围内的位置信息
     * @param location
     * @return gisHerbLocations
     */
    List<GisHerbLocation> findNearByHerbLocations(Location location);

    /**
     * 获得指定范围内的位置信息
     * @param location
     * @param radius
     * @return gisHerbLocations
     */
    List<GisHerbLocation> findNearByHerbLocations(Location location, double radius);

    GisHerbLocation transferDTOToGisHerbLocation(HerbLocationDTO herbLocationDTO);
    HerbLocationVO transferHerbLocationToVO(GisHerbLocation herbLocation);
    List<HerbLocationVO> transferHerbLocationListToVOList(List<GisHerbLocation> herbLocationList);
}
