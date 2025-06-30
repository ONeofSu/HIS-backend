package org.csu.herbinfo.controller;

import org.csu.herbinfo.DTO.HerbGrowthDTO;
import org.csu.herbinfo.VO.HerbGrowthVO;
import org.csu.herbinfo.entity.HerbGrowth;
import org.csu.herbinfo.service.HerbGrowthService;
import org.csu.herbinfo.service.HerbService;
import org.csu.herbinfo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/*
交互中药生长日志信息的Controller
 */

@RestController
public class HerbGrowthController {
    @Autowired
    HerbService herbService;
    @Autowired
    HerbGrowthService herbGrowthService;
    @Autowired
    UserService userService;

    //新增生长信息
    @PostMapping(value = "/growth")
    public ResponseEntity<?> addGrowth(@RequestHeader("Authorization") String authHeader,
                                       @RequestBody HerbGrowthDTO herbGrowthDTO) {
        if(!herbService.isHerbNameExist(herbGrowthDTO.getHerbName())) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","Herb does not exist!")
            );
        }

        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","invalid user")
            );
        }

        HerbGrowth herbGrowth = herbGrowthService.transferHerbGrowthDTOToHerbGrowth(herbGrowthDTO, userId);
        System.out.println(herbGrowth.getRecordTime());
        HerbGrowthVO herbGrowthVO = herbGrowthService.transferHerbGrowthToVO(herbGrowth);
        if(!herbGrowthService.addHerbGrowth(herbGrowth)){
            return ResponseEntity.internalServerError().body("error to add");
        }

        herbGrowthVO.setId(herbGrowthService.getHerbGrowthByHerbGrowthExceptId(herbGrowth).getId());

        return ResponseEntity.ok(
                Map.of("code",0,
                        "herbGrowth",herbGrowthVO)
        );
    }

    @GetMapping("/growth/all")
    public ResponseEntity<?> getAllGrowth() {
        List<HerbGrowth> herbGrowths = herbGrowthService.getAllHerbGrowths();
        List<HerbGrowthVO> herbGrowthVOS = herbGrowthService.transferGrowthListToVOList(herbGrowths);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "herbGrowths",herbGrowthVOS)
        );
    }

    @GetMapping("/growth/batch/{batchCode}")
    public ResponseEntity<?> getAllGrowthOnBatch(@PathVariable String batchCode) {
        if(!herbGrowthService.isBatchCodeExist(batchCode)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","Batch code does not exist!")
            );
        }

        List<HerbGrowth> herbGrowths = herbGrowthService.getHerbGrowthsByBatchCode(batchCode);
        List<HerbGrowthVO> herbGrowthVOS = herbGrowthService.transferGrowthListToVOList(herbGrowths);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "batchCode",batchCode,
                        "herbGrowths",herbGrowthVOS)
        );
    }

    @GetMapping("/growth/user/{userId}")
    public ResponseEntity<?> getAllGrowthOnUser(@PathVariable int userId) {
        if(!userService.isUserIdExist(userId)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","the user does not exist!")
            );
        }

        List<HerbGrowth> herbGrowths = herbGrowthService.getAllHerbGrowthByUserId(userId);
        List<HerbGrowthVO> herbGrowthVOS = herbGrowthService.transferGrowthListToVOList(herbGrowths);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "userId",userId,
                        "herbGrowths",herbGrowthVOS)
        );
    }

    @DeleteMapping("/growth/{growthId}")
    public ResponseEntity<?> deleteGrowth(@RequestHeader("Authorization") String authHeader,
                                          @PathVariable int growthId) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid user")
            );
        }

        HerbGrowth herbGrowth = herbGrowthService.getHerbGrowthById(growthId);
        if(herbGrowth == null){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","invalid growthId")
            );
        }

        if(herbGrowth.getUserId() != userId){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you can't delete growth record made by others")
            );
        }

        HerbGrowthVO herbGrowthVO = herbGrowthService.transferHerbGrowthToVO(herbGrowth);

        if(!herbGrowthService.deleteHerbGrowthById(growthId)){
            return ResponseEntity.internalServerError().body("error to delete");
        }
        return ResponseEntity.ok(
                Map.of("code",0,
                        "herbGrowth",herbGrowthVO)
        );
    }
}
