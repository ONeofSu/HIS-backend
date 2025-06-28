package org.csu.herbinfo;

import org.csu.herbinfo.DTO.HerbGrowthDTO;
import org.csu.herbinfo.entity.Herb;
import org.csu.herbinfo.entity.HerbCategory;
import org.csu.herbinfo.entity.HerbGrowth;
import org.csu.herbinfo.entity.HerbLocation;
import org.csu.herbinfo.service.HerbGrowthService;
import org.csu.herbinfo.service.HerbLocationService;
import org.csu.herbinfo.service.HerbService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Date;

@SpringBootTest
class HerbInfoApplicationTests {
    @Autowired
    HerbLocationService herbLocationService;

    @Test
    void contextLoads() {
        System.out.println(herbLocationService.getAllHerbLocations());
        System.out.println(herbLocationService.getHerbLocationsByHerbId(1));
        System.out.println(herbLocationService.getHerbLocationsByDistrict("渝中区"));
        System.out.println(herbLocationService.getHerbLocationsByStreet("太白街道"));
    }

}
