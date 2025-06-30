package org.csu.herbinfo.controller;

import org.csu.herbinfo.VO.DistrictCountVO;
import org.csu.herbinfo.VO.TopHerbVO;
import org.csu.herbinfo.service.DistrictStreetService;
import org.csu.herbinfo.service.HerbLocationService;
import org.csu.herbinfo.service.HerbService;
import org.csu.herbinfo.service.LocationCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/herbs/location/count")
public class LocationCountController {
    @Autowired
    LocationCountService locationCountService;

    @GetMapping("/districts")
    public ResponseEntity<?> getHerbNumsOnAllDistricts() {
        List<DistrictCountVO> districtCountVOS = locationCountService.getDistrictCount();
        return ResponseEntity.ok(
                Map.of(
                        "code",0,
                        "result",districtCountVOS
                )
        );
    }

    @GetMapping("topHerbs")
    public ResponseEntity<?> getTopHerbs() {
        List<TopHerbVO> topHerbVOS = locationCountService.getTopHerbs();
        return ResponseEntity.ok(
                Map.of(
                        "code",0,
                        "result",topHerbVOS
                )
        );
    }
}
