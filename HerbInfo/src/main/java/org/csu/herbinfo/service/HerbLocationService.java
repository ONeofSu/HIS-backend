package org.csu.herbinfo.service;

import org.csu.herbinfo.DTO.HerbLocationDTO;
import org.csu.herbinfo.entity.HerbLocation;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HerbLocationService {
    public List<HerbLocation> getAllHerbLocations();
    public List<HerbLocation> getHerbLocationsByHerbId(int herbId);
    public List<HerbLocation> getHerbLocationsByDistrictName(String district);
    public List<HerbLocation> getHerbLocationsByStreetName(String street);
    public boolean addHerbLocation(HerbLocation herbLocation);
    public boolean deleteHerbLocation(int herbLocationId);

    public boolean isHerbLocationExist(int herbLocationId);

    public HerbLocation transferDTOToHerbLocation(HerbLocationDTO herbLocationDTO);
}
