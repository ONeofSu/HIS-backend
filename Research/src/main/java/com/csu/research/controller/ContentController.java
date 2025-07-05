package com.csu.research.controller;

import com.csu.research.DTO.ContentDTO;
import com.csu.research.entity.Content;
import com.csu.research.entity.ContentBlock;
import com.csu.research.service.AuthService;
import com.csu.research.service.ContentService;
import com.csu.research.service.TopicService;
import com.csu.research.service.UserService;
import com.csu.research.vo.ContentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contents")
public class ContentController {
    @Autowired
    private ContentService contentService;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    @Autowired
    private TopicService topicService;

    @PostMapping("/")
    public ResponseEntity<?> addNewContent(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody ContentDTO contentDTO) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if (userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!topicService.isTopicExist(contentDTO.getTopicId())){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","topic not exist")
            );
        }

        Content content = contentService.transferDTOToContent(contentDTO, userId);

        if(!authService.isQualifiedToAddContent(content,userId) && !userService.isAdmin(userId)){
            //System.out.println(userId);
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you are not qualified to add content")
            );
        }

        content = contentService.addContent(content);
        ContentVo contentVo = contentService.transferToContentVo(content,true);
        return ResponseEntity.ok(Map.of("code",0,"content",contentVo));
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<?> deleteContent(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable("contentId") Long contentId) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if (userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!contentService.isContentExist(contentId)){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","content not exist")
            );
        }

        if(!authService.isQualifiedToWriteContent(contentId,userId) && !userService.isAdmin(userId)){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you are not qualified to delete content")
            );
        }

        contentService.recursionDeleteContent(contentId);
        return ResponseEntity.ok(Map.of("code",0,"contentId",contentId));
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<?> getContentOfTopic(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable("topicId") Long topicId) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if (userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        List<ContentVo> contents = contentService.findAllContentsOfOneTopic(topicId,true);
        System.out.println(contents);
        List<ContentVo> result = new ArrayList<>();

        for(ContentVo contentVo : contents){
            if(authService.isQualifiedToReadContent(contentVo.getContentId(),userId)
                    || userService.isAdmin(userId)){
                result.add(contentVo);  //有权限就增加
            }
        }

        return ResponseEntity.ok(Map.of("code",0,"contents",result));
    }

    @GetMapping("/topic/{topicId}/type/{typeName}")
    public ResponseEntity<?> getContentOfTopicAndType(@RequestHeader("Authorization") String authHeader,
                                                      @PathVariable("topicId") Long topicId,
                                                      @PathVariable("typeName") String typeName) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if (userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        Long typeId = contentService.getContentTypeIdByName(typeName);
        List<ContentVo> contentVos = contentService.findAllContentsOfOneTopicAndType(topicId,typeId,true);
        List<ContentVo> result = new ArrayList<>();

        for(ContentVo contentVo : contentVos){
            if(authService.isQualifiedToReadContent(contentVo.getContentId(),userId)
                    || userService.isAdmin(userId)){
                result.add(contentVo);  //有权限就增加
            }
        }

        return ResponseEntity.ok(Map.of("code",0,"contents",result));
    }

    @PutMapping("/{contentId}")
    public ResponseEntity<?> updateContent(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody ContentDTO contentDTO,
                                           @PathVariable("contentId") Long contentId) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if (userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!topicService.isTopicExist(contentDTO.getTopicId())){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","topic not exist")
            );
        }

        if(!contentService.isContentExist(contentId)){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","content not exist")
            );
        }

        if(!authService.isQualifiedToWriteContent(contentId,userId) && !userService.isAdmin(userId)){
            //System.out.println(authService.isQualifiedToWriteContent(contentId,userId));
            return ResponseEntity.ok(
                    Map.of("code",-4,
                            "message","you are not qualified to update content")
            );
        }

        //System.out.println(authService.isQualifiedToWriteContent(contentId,userId));
        Content content = contentService.transferDTOToContent(contentDTO, userId);
        content.setContentId(contentId);
        content.setUserId(contentService.getContent(contentId).getUserId());    //用户id不变
        if(!contentService.updateContent(content)){
            return ResponseEntity.ok(
                    Map.of("code",-5,
                            "message","failed to update content")
            );
        }

        return ResponseEntity.ok(Map.of("code",0,"contentId",contentId));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> getContentOfTeam(@RequestHeader("Authorization") String authHeader,
                                              @PathVariable("teamId") Long teamId) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if (userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        List<ContentVo> contentVos = contentService.findAllSimpleContentsOfOneTeam(teamId);
        System.out.println(contentVos);
        List<ContentVo> result = new ArrayList<>();
        for(ContentVo contentVo : contentVos){
            if(authService.isQualifiedToReadContent(contentVo.getContentId(),userId)
                    || userService.isAdmin(userId)){
                result.add(contentVo);  //有权限就增加
            }
        }

        return ResponseEntity.ok(Map.of("code",0,"contents",result));
    }

    @PostMapping("/{contentId}/details")
    public ResponseEntity<?> addContentDetails(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable("contentId") Long contentId,
                                               @RequestBody List<ContentBlock> contentBlocks) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if (userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!contentService.isContentExist(contentId)){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","content not exist")
            );
        }

        if(!authService.isQualifiedToWriteContent(contentId,userId) && !userService.isAdmin(userId)){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you are not qualified to add content")
            );
        }

        for(ContentBlock contentBlock : contentBlocks){
            contentBlock.setContentId(contentId);
            contentService.addContentBlock(contentBlock);
        }
        Content content = contentService.getContent(contentId);
        ContentVo contentVo = contentService.transferToContentVo(content,false);
        return ResponseEntity.ok(Map.of("code",0,"contents",contentVo));
    }

    @PutMapping("/{contentId}/details")
    public ResponseEntity<?> updateContentDetails(@RequestHeader("Authorization") String authHeader,
                                                  @PathVariable("contentId") Long contentId,
                                                  @RequestBody List<ContentBlock> contentBlocks){
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if (userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!contentService.isContentExist(contentId)){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","content not exist")
            );
        }

        if(!authService.isQualifiedToWriteContent(contentId,userId) && !userService.isAdmin(userId)){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you are not qualified to update content")
            );
        }

        //删除原有的内容
        List<ContentBlock> ori = contentService.getAllContentBlockOnContent(contentId);
        for(ContentBlock contentBlock : ori){
            contentService.deleteContentBlock(contentBlock.getContentBlockId());
        }
        //添加现有内容
        for(ContentBlock contentBlock : contentBlocks){
            contentBlock.setContentId(contentId);
            contentService.addContentBlock(contentBlock);
        }
        List<ContentBlock> result = contentService.getAllContentBlockOnContent(contentId);
        return ResponseEntity.ok(Map.of("code",0,"contents",result));
    }

    @GetMapping("/{contentId}/details")
    public ResponseEntity<?> getContentDetails(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable("contentId") Long contentId){
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if (userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!contentService.isContentExist(contentId)){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","content not exist")
            );
        }

        if(!authService.isQualifiedToReadContent(contentId,userId) && !userService.isAdmin(userId)){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you are not qualified to read content")
            );
        }

        Content content = contentService.getContent(contentId);
        ContentVo contentVo = contentService.transferToContentVo(content,false);

        return ResponseEntity.ok(Map.of("code",0,"contents",contentVo));
    }
}