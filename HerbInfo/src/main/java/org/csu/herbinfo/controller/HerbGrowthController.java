package org.csu.herbinfo.controller;

import org.csu.herbinfo.DTO.HerbGrowthDTO;
import org.csu.herbinfo.service.HerbGrowthService;
import org.csu.herbinfo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/*
交互中药生长日志信息的Controller
 */

@RestController
public class HerbGrowthController {
    @Autowired
    HerbGrowthService herbGrowthService;
    @Autowired
    UserService userService;

    //新增生长信息
    @PostMapping(value = "/growth")
    public ResponseEntity<?> addGrowth(@RequestHeader("Authorization") String authHeader,
                                       @RequestBody HerbGrowthDTO herbGrowthDTO) {
        String token = authHeader.substring(7);
        System.out.println(userService.getUserId(token));
        return null;
    }

    @GetMapping("/growth")
    public ResponseEntity<?> getAllGrowth(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        System.out.println(userService.getUserId(token));
        return null;
    }

    @GetMapping("/growth/batch/{batchCode}")
    public ResponseEntity<?> getAllGrowthOnBatch(@PathVariable String batchCode) {
        return null;
    }

    @DeleteMapping("/growth/{growth_id}")
    public ResponseEntity<?> deleteGrowth(@PathVariable String growth_id) {
        return null;
    }

}
