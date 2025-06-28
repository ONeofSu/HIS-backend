package org.csu.herbinfo.controller;

import org.csu.herbinfo.DTO.DistrictAndStreetDTO;
import org.csu.herbinfo.DTO.HerbLocationDTO;
import org.csu.herbinfo.entity.HerbLocation;
import org.csu.herbinfo.service.DistrictStreetService;
import org.csu.herbinfo.service.HerbLocationService;
import org.csu.herbinfo.service.HerbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
交互中药位置信息的Controller
 */

@RestController
public class HerbInfoLocationController {
    @Autowired
    HerbService herbService;
    @Autowired
    HerbLocationService herbLocationService;
    @Autowired
    DistrictStreetService districtStreetService;

    @GetMapping("/herbs/location")
    public ResponseEntity<?> getAllHerbLocation() {
        ResponseEntity<List<HerbLocation>> result = null;
        List<HerbLocation> locations = herbLocationService.getAllHerbLocations();
        result = ResponseEntity.ok(locations);
        return result;
    }

    @GetMapping("/herbs/location/herbname/{name}")
    public ResponseEntity<?> getAllHerbLocationByHerbName(@PathVariable String name) {
        ResponseEntity<List<HerbLocation>> result = null;
        int id = herbService.getHerbIdByName(name);
        if(id == -1){
            return ResponseEntity.status(490).body("中药不存在");
        }
        List<HerbLocation> locations = herbLocationService.getHerbLocationsByHerbId(id);
        result = ResponseEntity.ok(locations);
        return result;
    }

    @GetMapping("/herbs/location/district/{district}")
    public ResponseEntity<?> getAllHerbLocationByDistrict(@PathVariable String district) {
        ResponseEntity<List<HerbLocation>> result = null;
        int district_id = districtStreetService.getDistrictIdByName(district);
        if(district_id == -1){
            return ResponseEntity.status(490).body("行政区不存在");
        }
        List<HerbLocation> locations = herbLocationService.getHerbLocationsByDistrictName(district);
        result = ResponseEntity.ok(locations);
        return result;
    }

    @GetMapping("/herbs/location/street/{street}")
    public ResponseEntity<?> getAllHerbLocationByStreet(@PathVariable String street) {
        ResponseEntity<List<HerbLocation>> result = null;
        int street_id = districtStreetService.getStreetIdByName(street);
        if(street_id == -1){
            return ResponseEntity.status(490).body("街道不存在");
        }
        List<HerbLocation> locations = herbLocationService.getHerbLocationsByStreetName(street);
        result = ResponseEntity.ok(locations);
        return result;
    }

    @PostMapping("/herbs/location")
    public ResponseEntity<?> addHerbLocation(@RequestBody HerbLocationDTO herbLocationDTO) {
        int herb_id = herbService.getHerbIdByName(herbLocationDTO.getName());
        if(herb_id == -1){
            return ResponseEntity.status(490).body("中药不存在");
        }

        int district_id = districtStreetService.getDistrictIdByName(herbLocationDTO.getDistrict());
        if(district_id == -1){
            return ResponseEntity.status(491).body("行政区不存在");
        }

        int street_id = districtStreetService.getStreetIdByName(herbLocationDTO.getStreet());
        if(street_id == -1){
            return ResponseEntity.status(492).body("街道不存在");
        }

        if(!districtStreetService.isStreetInDistrict(district_id, street_id)){
            return ResponseEntity.status(493).body("该街道不在该行政区中");
        }

        HerbLocation herbLocation = herbLocationService.transferDTOToHerbLocation(herbLocationDTO);
        System.out.println(herbLocation);
        if(!herbLocationService.addHerbLocation(herbLocation)){
            return ResponseEntity.status(500).body("插入失败");
        }
        return ResponseEntity.ok(herbLocation);
    }

    @DeleteMapping("/herbs/location/{locationId}")
    public ResponseEntity<?> deleteHerbLocation(@PathVariable int locationId) {
        if(!herbLocationService.isHerbLocationExist(locationId)){
            return ResponseEntity.status(490).body("该位置信息不存在");
        }
        if(!herbLocationService.deleteHerbLocation(locationId)){
            return ResponseEntity.status(500).body("删除失败");
        }
        return ResponseEntity.ok(locationId);
    }

    @PostMapping("/location/valid")
    public ResponseEntity<?> validDistrictAndStreet(@RequestBody DistrictAndStreetDTO districtAndStreetDTO) {
        int district_id = districtStreetService.getDistrictIdByName(districtAndStreetDTO.getDistrict());
        if(district_id == -1){
            return ResponseEntity.status(490).body("行政区不存在");
        }
        int street_id = districtStreetService.getStreetIdByName(districtAndStreetDTO.getStreet());
        if(street_id == -1){
            return ResponseEntity.status(491).body("街道不存在");
        }

        if(!districtStreetService.isStreetInDistrict(district_id, street_id)){
            return ResponseEntity.status(492).body("街道不在行政区中");
        }

        return ResponseEntity.ok(districtAndStreetDTO);
    }
}
