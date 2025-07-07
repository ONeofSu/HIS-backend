package org.csu.herbinfo.controller;

import org.csu.herbinfo.DTO.GrowthAuditDTO;
import org.csu.herbinfo.VO.HerbGrowthVO;
import org.csu.herbinfo.entity.GrowthAudit;
import org.csu.herbinfo.entity.HerbGrowth;
import org.csu.herbinfo.service.HerbGrowthService;
import org.csu.herbinfo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/root/audit")
public class GrowthAuditController {
    @Autowired
    HerbGrowthService herbGrowthService;
    @Autowired
    UserService userService;

    @GetMapping("/growth/all")
    public ResponseEntity<?> getGrowthAll(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if(!userService.isAdminToken(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        List<HerbGrowth> growths = herbGrowthService.getAllHerbGrowths();
        List<HerbGrowthVO> growthVOS = herbGrowthService.transferGrowthListToVOList(growths);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "growths",growthVOS)
        );
    }

    @GetMapping("/growth/unaudited")
    public ResponseEntity<?> getGrowthUnaudited(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if(!userService.isAdminToken(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        List<HerbGrowth> growths = herbGrowthService.getAllHerbGrowthsNeedToAudit();
        List<HerbGrowthVO> growthVOS = herbGrowthService.transferGrowthListToVOList(growths);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "growths",growthVOS)
        );
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAudit(@RequestHeader("Authorization") String authHeader,
                                      @RequestBody GrowthAuditDTO growthAuditDTO) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(!userService.isAdminToken(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }
        if(!herbGrowthService.isHerbGrowthExist(growthAuditDTO.getGrowthId())){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","growth id does not exist")
            );
        }
        if(herbGrowthService.getHerbGrowthById(growthAuditDTO.getGrowthId()).getGrowthAuditStatus()!=0){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","growth id already audited")
            );
        }

        GrowthAudit growthAudit = herbGrowthService.transferDTOToAudit(growthAuditDTO,userId);
        growthAudit = herbGrowthService.handleAudit(growthAudit);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "growths",growthAudit)
        );
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllAudit(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if(!userService.isAdminToken(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        List<GrowthAudit> audits = herbGrowthService.getAllAudit();
        return ResponseEntity.ok(
                Map.of("code",0,
                        "audits",audits)
        );
    }

    @PutMapping("/{auditId}")
    public ResponseEntity<?> updateAudit(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody GrowthAuditDTO growthAuditDTO,
                                         @PathVariable Long auditId) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(!userService.isAdminToken(token)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }
        if(!herbGrowthService.isAuditExist(auditId)) {
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","audit id does not exist")
            );
        }
        if(!herbGrowthService.isHerbGrowthExist(growthAuditDTO.getGrowthId())) {
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","growth id does not exist")
            );
        }
        GrowthAudit ori = herbGrowthService.getAuditById(auditId);
        if(ori.getGrowthId()!=growthAuditDTO.getGrowthId()){
            return ResponseEntity.ok(
                    Map.of("code",-4,
                            "message","you can't change growth id")
            );
        }

        GrowthAudit growthAudit = herbGrowthService.transferDTOToAudit(growthAuditDTO,userId);
        growthAudit.setAuditId(auditId);
        growthAudit = herbGrowthService.updateAudit(growthAudit);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "growths",growthAudit)
        );
    }
}
