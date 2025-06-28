package org.csu.herbinfo.controller;

import org.csu.herbinfo.entity.HerbLocation;
import org.csu.herbinfo.service.HerbGrowthService;
import org.csu.herbinfo.service.HerbLocationService;
import org.csu.herbinfo.service.HerbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
交互中药应用的Controller
 */

@RestController
public class HerbInfoApplyController {
    @Autowired
    HerbService herbService;
    @Autowired
    HerbGrowthService herbGrowthService;
    @Autowired
    HerbLocationService herbLocationService;

    @GetMapping("/herbs/location")
    public ResponseEntity<?> getAllHerbLocation() {
        ResponseEntity<List<HerbLocation>> result = null;
        List<HerbLocation> locations = herbLocationService.getAllHerbLocations();
        result = ResponseEntity.ok(locations);
        return result;
    }

    @GetMapping("/herbs/location/herbname/{name}")
    public ResponseEntity<?> getAllHerbLocationByHerbname(@PathVariable String name) {
        ResponseEntity<List<HerbLocation>> result = null;
        int id = herbService.getHerbIdByName(name);
        if(id == -1){
            return ResponseEntity.status(490).body("中药不存在");
        }
        List<HerbLocation> locations = herbLocationService.getHerbLocationsByHerbId(id);
        result = ResponseEntity.ok(locations);
        return result;
    }

}
