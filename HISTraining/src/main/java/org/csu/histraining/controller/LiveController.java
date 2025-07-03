package org.csu.histraining.controller;

import lombok.extern.slf4j.Slf4j;
import org.csu.histraining.DTO.LiveRoomDTO;
import org.csu.histraining.entity.LiveRoom;
import org.csu.histraining.mapper.LiveRoomMapper;
import org.csu.histraining.service.LiveRecordService;
import org.csu.histraining.service.LiveStreamService;
import org.csu.histraining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class LiveController {
    @Autowired
    LiveStreamService liveStreamService;
    @Autowired
    LiveRecordService liveRecordService;
    @Autowired
    LiveRoomMapper liveRoomMapper;
    @Autowired
    UserService userService;

    @PostMapping("/live/room")
    public ResponseEntity<?> createLiveRoom(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody LiveRoomDTO liveRoomDTO) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        LiveRoom liveRoom = new LiveRoom();
        liveRoom.setTitle(liveRoomDTO.getTitle());
        liveRoom.setCoverUrl(liveRoomDTO.getCoverUrl());
        liveRoom.setUserId(userId);
        liveRoom = liveStreamService.createLiveRoom(liveRoom);

        return ResponseEntity.ok(
                Map.of("code",0,
                        "liveRoom",liveRoom)
        );
    }

    @GetMapping("/live/room/{roomId}")
    public ResponseEntity<?> getLiveRoom(@PathVariable("roomId") int roomId) {
        LiveRoom liveRoom = liveRoomMapper.selectById(roomId);
        if(liveRoom == null){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid roomId")
            );
        }

        return ResponseEntity.ok(
                Map.of("code",0,
                        "liveRoom",liveRoom)
        );
    }

    @PostMapping("/live/room/{roomId}/start")
    public ResponseEntity<?> startLiveRoom(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable("roomId") Long roomId) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        LiveRoom liveRoom = liveRoomMapper.selectById(roomId);
        if(liveRoom == null){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","invalid roomId")
            );
        }

        if(userId!=liveRoom.getUserId()){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you can't start live on other user's room")
            );
        }

        liveRoom = liveStreamService.startLiveStream(roomId);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "liveRoom",liveRoom)
        );
    }

    @PostMapping("/live/room/{roomId}/end")
    public ResponseEntity<?> endLiveRoom(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable("roomId") Long roomId) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        LiveRoom liveRoom = liveRoomMapper.selectById(roomId);
        if(liveRoom == null){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","invalid roomId")
            );
        }

        if(userId!=liveRoom.getUserId()){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you can't start live on other user's room")
            );
        }

        liveRoom = liveStreamService.endLiveStream(roomId);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "liveRoom",liveRoom)
        );
    }

    @GetMapping("/live/rooms/active")
    public ResponseEntity<?> getLiveRoomActive(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<LiveRoom> rooms = liveStreamService.getActiveLiveRooms(page, size);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "rooms",rooms)
        );
    }

    @GetMapping("/live/rooms/hot")
    public ResponseEntity<?> getLiveRoomHot(
            @RequestParam(defaultValue = "10") int limit){
        List<LiveRoom> rooms = liveStreamService.getHotLiveRooms(limit);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "rooms",rooms)
        );
    }

    @PostMapping("/live/room/{roomId}/view")
    public ResponseEntity<?> viewLiveRoom(@PathVariable Long roomId) {
        LiveRoom liveRoom = liveRoomMapper.selectById(roomId);
        if(liveRoom == null){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid roomId")
            );
        }

        liveStreamService.addViewCount(roomId);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "roomId",roomId)
        );
    }

}
