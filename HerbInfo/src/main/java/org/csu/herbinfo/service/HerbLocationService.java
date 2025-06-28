package org.csu.herbinfo.service;

import org.csu.herbinfo.entity.HerbLocation;
import org.springframework.stereotype.Service;

import java.util.List;

public interface HerbLocationService {
    public List<HerbLocation> getAllHerbLocations();
    public List<HerbLocation> getHerbLocationsByHerbId(int herbId);
    //public List<HerbLocation> getHerbLocationsByDistrict(String district);
    //public List<HerbLocation> getHerbLocationsByStreet(String street);

    public boolean addHerbLocation(HerbLocation herbLocation);
}
