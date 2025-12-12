package com.csu.research.controller;

import com.csu.research.DTO.DocumentDTO;
import com.csu.research.entity.Document;
import com.csu.research.service.AuthService;
import com.csu.research.service.DocumentService;
import com.csu.research.service.TopicService;
import com.csu.research.service.UserService;
import com.csu.research.vo.DocumentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class DocumentController {
    @Autowired
    private UserService userService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private AuthService authService;
    @Autowired
    private TopicService topicService;

    @PostMapping("/")
    public ResponseEntity<?> addDocument(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody DocumentDTO documentDTO) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if (userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!topicService.isTopicExist(documentDTO.getTopicId())){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","topic does not exist")
            );
        }

        Document document = documentService.transferDTOToDocument(documentDTO,userId);
        if(!authService.isQualifiedToAddDocument(document,userId) && !userService.isAdmin(userId)){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you cannot add in this topic")
            );
        }

        document = documentService.addDocument(document);
        DocumentVo documentVo = documentService.transferDocumentToVo(document);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "document",documentVo)
        );
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<?> updateDocument(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody DocumentDTO documentDTO,
                                            @PathVariable Long documentId) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if (userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!documentService.isDocumentExist(documentId)){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","document does not exist")
            );
        }

        if(!topicService.isTopicExist(documentDTO.getTopicId())){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","topic does not exist")
            );
        }

        if(!authService.isQualifiedToWriteDocument(documentId,userId) &&
                !userService.isAdmin(userId)){
            return ResponseEntity.ok(
                    Map.of("code",-4,
                            "message","you cannot update the document")
            );
        }

        Document ori = documentService.getDocumentById(documentId);
        Document document = documentService.transferDTOToDocument(documentDTO,userId);
        document.setDocumentId(documentId);
        document.setUserId(ori.getUserId());    //不修改创建者
        documentService.updateDocument(document);
        DocumentVo documentVo = documentService.transferDocumentToVo(document);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "document",documentVo)
        );
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<?> deleteDocument(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable Long documentId){
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if (userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!documentService.isDocumentExist(documentId)){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","document does not exist")
            );
        }

        if(!authService.isQualifiedToWriteDocument(documentId,userId) &&
                !userService.isAdmin(userId)){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you cannot update the document")
            );
        }

        documentService.deleteDocument(documentId);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "documentId",documentId)
        );
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<?> getDocument(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable Long documentId){
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if (userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!documentService.isDocumentExist(documentId)){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","document does not exist")
            );
        }

        if(!authService.isQualifiedToReadDocument(documentId,userId) &&  !userService.isAdmin(userId)){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you cannot read the document")
            );
        }

        Document document = documentService.getDocumentById(documentId);
        DocumentVo documentVo = documentService.transferDocumentToVo(document);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "document",documentVo)
        );
    }

    @GetMapping("/topic/{topicId}/all")
    public ResponseEntity<?> getAllDocsOfTopic(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable Long topicId){
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if (userId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        List<Document> documents = documentService.getAllDocumentsOfTopic(topicId);
        List<DocumentVo> documentVos = documentService.transferDocumentsToVo(documents);
        List<DocumentVo> result = new ArrayList<>();
        for (DocumentVo documentVo : documentVos) {
            if(authService.isQualifiedToReadDocument(documentVo.getDocumentId(),userId)
            || userService.isAdmin(userId)){
                result.add(documentVo);
            }
        }
        return ResponseEntity.ok(
                Map.of("code",0,
                        "documents",result)
        );
    }
}
