package org.csu.herbinfo;

import org.csu.herbinfo.DTO.HerbGrowthDTO;
import org.csu.herbinfo.DTO.Location;
import org.csu.herbinfo.entity.*;
import org.csu.herbinfo.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@SpringBootTest
class HerbInfoApplicationTests {
    @Autowired
    GisHerbLocationService gisHerbLocationService;

    @Test
    void contextLoads() {
        List<GisHerbLocation> locations = gisHerbLocationService.findNearByHerbLocations(new Location(107.8,30.68),100000);
        System.out.println(locations);
    }

}
