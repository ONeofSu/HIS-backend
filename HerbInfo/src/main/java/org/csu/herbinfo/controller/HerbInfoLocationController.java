package org.csu.herbinfo.controller;

import org.csu.herbinfo.DTO.DistrictAndStreetDTO;
import org.csu.herbinfo.DTO.HerbLocationDTO;
import org.csu.herbinfo.VO.HerbLocationVO;
import org.csu.herbinfo.VO.StreetVO;
import org.csu.herbinfo.entity.District;
import org.csu.herbinfo.entity.Herb;
import org.csu.herbinfo.entity.HerbLocation;
import org.csu.herbinfo.entity.Street;
import org.csu.herbinfo.service.DistrictStreetService;
import org.csu.herbinfo.service.HerbLocationService;
import org.csu.herbinfo.service.HerbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        List<HerbLocation> locations = herbLocationService.getAllHerbLocations();
        List<HerbLocationVO> locationVOs = herbLocationService.transferHerbLocationListToVOList(locations);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "locations",locationVOs)
        );
    }

    @GetMapping("/herbs/location/herbname/{name}")
    public ResponseEntity<?> getAllHerbLocationByHerbName(@PathVariable String name) {
        int id = herbService.getHerbIdByName(name);
        if(id == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","herb does not exist")
            );
        }
        List<HerbLocation> locations = herbLocationService.getHerbLocationsByHerbId(id);
        List<HerbLocationVO> locationVOs = herbLocationService.transferHerbLocationListToVOList(locations);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "locations",locationVOs)
        );
    }

    @GetMapping("/herbs/location/district/{districtName}")
    public ResponseEntity<?> getAllHerbLocationByDistrict(@PathVariable String districtName) {
        int district_id = districtStreetService.getDistrictIdByName(districtName);
        if(district_id == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","district does not exist")
            );
        }
        List<HerbLocation> locations = herbLocationService.getHerbLocationsByDistrictName(districtName);
        List<HerbLocationVO> locationVOs = herbLocationService.transferHerbLocationListToVOList(locations);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "locations",locationVOs)
        );
    }

    @GetMapping("/herbs/location/street/{streetName}")
    public ResponseEntity<?> getAllHerbLocationByStreet(@PathVariable String streetName) {
        int street_id = districtStreetService.getStreetIdByName(streetName);
        if(street_id == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","street does not exist")
            );
        }
        List<HerbLocation> locations = herbLocationService.getHerbLocationsByStreetName(streetName);
        List<HerbLocationVO> locationVOs = herbLocationService.transferHerbLocationListToVOList(locations);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "locations",locationVOs)
        );
    }

    @PostMapping("/herbs/location")
    public ResponseEntity<?> addHerbLocation(@RequestBody HerbLocationDTO herbLocationDTO) {
        int herb_id = herbService.getHerbIdByName(herbLocationDTO.getName());
        if(herb_id == -1){
            //return ResponseEntity.status(490).body("中药不存在");
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","herb does not exist")
            );
        }

        int district_id = districtStreetService.getDistrictIdByName(herbLocationDTO.getDistrict());
        if(district_id == -1){
            //return ResponseEntity.status(491).body("行政区不存在");
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","district does not exist")
            );
        }

        int street_id = districtStreetService.getStreetIdByName(herbLocationDTO.getStreet());
        if(street_id == -1){
            //return ResponseEntity.status(492).body("街道不存在");
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","street does not exist")
            );
        }

        if(!districtStreetService.isStreetInDistrict(district_id, street_id)){
            return ResponseEntity.ok(
                    Map.of("code",-4,
                            "message","street does not in the district")
            );
        }

        HerbLocation herbLocation = herbLocationService.transferDTOToHerbLocation(herbLocationDTO);
        System.out.println(herbLocation);
        if(!herbLocationService.addHerbLocation(herbLocation)){
            return ResponseEntity.status(500).body("error to insert");
        }
        herbLocation = herbLocationService.getHerbLocationByLocationInfo(herbLocation);
        System.out.println(herbLocation);
        HerbLocationVO herbLocationVO = herbLocationService.transferHerbLocationToVO(herbLocation);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "location",herbLocationVO)
        );
    }

    @DeleteMapping("/herbs/location/{locationId}")
    public ResponseEntity<?> deleteHerbLocation(@PathVariable int locationId) {
        if(!herbLocationService.isHerbLocationExist(locationId)){
            //return ResponseEntity.status(490).body("该位置信息不存在");
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","the location does not exist")
            );
        }
        HerbLocation location = herbLocationService.getHerbLocationById(locationId);
        HerbLocationVO locationVO = herbLocationService.transferHerbLocationToVO(location);
        if(!herbLocationService.deleteHerbLocation(locationId)){
            return ResponseEntity.status(500).body("error to delete");
        }
        return ResponseEntity.ok(
                Map.of("code",0,
                        "location",locationVO)
        );
    }

    @PostMapping("/location/valid")
    public ResponseEntity<?> validDistrictAndStreet(@RequestBody DistrictAndStreetDTO districtAndStreetDTO) {
        int district_id = districtStreetService.getDistrictIdByName(districtAndStreetDTO.getDistrict());
        if(district_id == -1){
            //return ResponseEntity.status(490).body("行政区不存在");
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","the district does not exist")
            );
        }
        int street_id = districtStreetService.getStreetIdByName(districtAndStreetDTO.getStreet());
        if(street_id == -1){
            //return ResponseEntity.status(491).body("街道不存在");
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","the street does not exist")
            );
        }

        if(!districtStreetService.isStreetInDistrict(district_id, street_id)){
            //return ResponseEntity.status(492).body("街道不在行政区中");
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","the street does not in the district")
            );
        }

        return ResponseEntity.ok(
                Map.of("code",0)
        );
    }

    @GetMapping("/division/district")
    public ResponseEntity<?> getDivisionDistrict() {
        List<District> districts = districtStreetService.getAllDistricts();
        return ResponseEntity.ok(
                Map.of("code",0,
                        "districts",districts)
        );
    }

    @GetMapping("/division/{districtName}/street")
    public ResponseEntity<?> getDivisionStreet(@PathVariable String districtName) {
        int district_id = districtStreetService.getDistrictIdByName(districtName);
        if(!districtStreetService.isDistrictExist(district_id)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","the district does not exist")
            );
        }
        List<Street> streets = districtStreetService.getAllStreetsInDistrictByDistrictId(district_id);
        List<StreetVO> streetVOS = districtStreetService.transferStreetToVOList(streets);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "streets",streetVOS)
        );
    }
}
