package org.csu.herbinfo.controller;

import org.csu.herbinfo.DTO.HerbGrowthDTO;
import org.csu.herbinfo.entity.HerbGrowth;
import org.csu.herbinfo.service.HerbGrowthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/*
交互中药基本信息的Controller
 */

@RestController
public class HerbInfoController {
    @Autowired
    HerbGrowthService herbGrowthService;

    //新增生长信息
    @PostMapping(value = "/growth",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addHerbGrowthInfo( @RequestParam("herbId") int herbId,@RequestParam("batchCode") String batchCode,
                                                     @RequestParam("wet") double wet,@RequestParam("temperature") double temperature,
                                                     @RequestParam("des") String des,@RequestParam("longitude") double longitude,
                                                     @RequestParam("latitude") double latitude,@RequestParam("userId") int userId,
                                                     @RequestParam("img") MultipartFile img){
        HerbGrowthDTO herbGrowthDTO = new HerbGrowthDTO();
        herbGrowthDTO.setHerbId(herbId);
        herbGrowthDTO.setBatchCode(batchCode);
        herbGrowthDTO.setWet(wet);
        herbGrowthDTO.setTemperature(temperature);
        herbGrowthDTO.setDes(des);
        herbGrowthDTO.setLongitude(longitude);
        herbGrowthDTO.setLatitude(latitude);
        herbGrowthDTO.setUserId(userId);
        herbGrowthDTO.setImg(img);
        if(!herbGrowthService.addHerbGrowth(herbGrowthDTO)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok("Already record growth info");
    }
}
