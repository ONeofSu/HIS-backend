package org.csu.histraining.controller;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.csu.histraining.DTO.SrsCallbackDTO;
import org.csu.histraining.Utils.HttpUtil;
import org.csu.histraining.service.LiveStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/srs/callback")
public class SrsCallbackController {
    @Autowired
    LiveStreamService liveStreamService;

    @PostMapping("/on_publish")
    public ResponseEntity<Map<String, Object>> onPublish(@RequestBody SrsCallbackDTO callbackDto) {
        log.info("SRS on_publish回调: app={}, stream={}", callbackDto.getApp(), callbackDto.getStream());
        System.out.println("回调");

        Map<String, Object> result = new HashMap<>();

        String param = callbackDto.getParam();
        Map<String, String> paramMap = HttpUtil.decodeParamMap(param, String.valueOf(StandardCharsets.UTF_8));
        String token = paramMap.get("auth_key");
        String expire = paramMap.get("expire");
        callbackDto.setToken(token);
        callbackDto.setExpire(expire);

        // 验证推流密钥
        boolean valid = liveStreamService.isStreamKeyValid(
                callbackDto.getStream(),
                callbackDto.getToken(),
                callbackDto.getExpire());

        if (!valid) {
            result.put("code", 403);
            result.put("message", "Forbidden");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }

        // 处理流发布事件
        liveStreamService.handleStreamPublish(callbackDto.getApp(), callbackDto.getStream());

        result.put("code", 0);
        result.put("message", "Success");
        return ResponseEntity.ok(result);
    }

    /**
     * 处理SRS on_unpublish回调
     * 当推流结束时，SRS会调用此接口
     */
    @PostMapping("/on_unpublish")
    public ResponseEntity<Map<String, Object>> onUnpublish(@RequestBody SrsCallbackDTO callbackDto) {
        log.info("SRS on_unpublish回调: app={}, stream={}", callbackDto.getApp(), callbackDto.getStream());

        // 处理流关闭事件
        liveStreamService.handleStreamClose(callbackDto.getApp(), callbackDto.getStream());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", "Success");
        return ResponseEntity.ok(result);
    }

    /**
     * 处理SRS on_play回调
     * 当播放流开始时，SRS会调用此接口
     */
    @PostMapping("/on_play")
    public ResponseEntity<Map<String, Object>> onPlay(@RequestBody SrsCallbackDTO callbackDto) {
        log.info("SRS on_play回调: app={}, stream={}", callbackDto.getApp(), callbackDto.getStream());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", "Success");
        return ResponseEntity.ok(result);
    }

    /**
     * 处理SRS on_stop回调
     * 当播放流结束时，SRS会调用此接口
     */
    @PostMapping("/on_stop")
    public ResponseEntity<Map<String, Object>> onStop(@RequestBody SrsCallbackDTO callbackDto) {
        log.info("SRS on_stop回调: app={}, stream={}", callbackDto.getApp(), callbackDto.getStream());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", "Success");
        return ResponseEntity.ok(result);
    }

    /**
     * 处理SRS on_dvr回调
     * 当DVR录制文件关闭时，SRS会调用此接口
     */
    @PostMapping("/on_dvr")
    public ResponseEntity<Map<String, Object>> onDvr(@RequestBody SrsCallbackDTO callbackDto) {
        log.info("SRS on_dvr回调: app={}, stream={}, file={}",
                callbackDto.getApp(), callbackDto.getStream(), callbackDto.getFile());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 0);
        result.put("message", "Success");
        return ResponseEntity.ok(result);
    }
}
