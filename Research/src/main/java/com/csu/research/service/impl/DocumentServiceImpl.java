package com.csu.research.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csu.research.DTO.DocumentDTO;
import com.csu.research.entity.Auth;
import com.csu.research.entity.Document;
import com.csu.research.mapper.AuthMapper;
import com.csu.research.mapper.DocumentMapper;
import com.csu.research.mapper.TeamMapper;
import com.csu.research.service.DocumentService;
import com.csu.research.service.TopicService;
import com.csu.research.vo.DocumentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    DocumentMapper documentMapper;
    @Autowired
    TopicService topicService;
    @Autowired
    AuthMapper authMapper;

    @Override
    public Document addDocument(Document document) {
        document.setDocumentTime(LocalDateTime.now());
        document.setDocumentIsValid(true);
        documentMapper.insert(document);
        return document;
    }

    @Override
    public boolean deleteDocument(Long id) {
        Document document = documentMapper.selectById(id);
        document.setDocumentIsValid(false);
        documentMapper.updateById(document);
        return true;
    }

    @Override
    public Document getDocumentById(Long id) {
        Document document = documentMapper.selectById(id);
        if(document==null || !document.getDocumentIsValid()){
            return null;
        }
        return document;
    }

    @Override
    public boolean updateDocument(Document document) {
        document.setDocumentIsValid(true);
        documentMapper.updateById(document);
        return true;
    }

    @Override
    public List<Document> getAllDocumentsOfTopic(Long topicId) {
        QueryWrapper<Document> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("topic_id", topicId).eq("document_isvalid", true);
        return documentMapper.selectList(queryWrapper);
    }

    @Override
    public boolean isDocumentExist(Long id) {
        Document document = documentMapper.selectById(id);
        return document != null && document.getDocumentIsValid();
    }

    @Override
    public DocumentVo transferDocumentToVo(Document document) {
        DocumentVo documentVo = new DocumentVo();
        documentVo.setDocumentId(document.getDocumentId());
        documentVo.setTopicId(document.getTopicId());
        documentVo.setUserId(document.getUserId());

        documentVo.setDocumentName(document.getDocumentName());
        documentVo.setDocumentDes(document.getDocumentDes());
        documentVo.setDocumentType(document.getDocumentType());
        documentVo.setDocumentUrl(document.getDocumentUrl());
        documentVo.setDocumentTime(document.getDocumentTime());

        documentVo.setTopicName(topicService.getTopicById(document.getTopicId()).getTopicName());

        return documentVo;
    }

    @Override
    public List<DocumentVo> transferDocumentsToVo(List<Document> documents) {
        List<DocumentVo> documentVos = new ArrayList<>();
        for (Document document : documents) {
            documentVos.add(transferDocumentToVo(document));
        }
        return documentVos;
    }

    @Override
    public Document transferDTOToDocument(DocumentDTO documentDTO,int userId) {
        Document document = new Document();
        document.setTopicId(documentDTO.getTopicId());
        document.setDocumentName(documentDTO.getDocumentName());
        document.setDocumentDes(documentDTO.getDocumentDes());
        document.setDocumentType(documentDTO.getDocumentType());
        document.setDocumentUrl(documentDTO.getDocumentUrl());
        document.setDocumentTime(LocalDateTime.now());
        document.setDocumentIsValid(true);
        document.setUserId(userId);

        QueryWrapper<Auth> topicQueryWrapper = new QueryWrapper<>();
        topicQueryWrapper.eq("auth_name",documentDTO.getAuth());
        Auth auth = authMapper.selectOne(topicQueryWrapper);
        if(auth==null){
            document.setAuthId((long)3);
        }else {
            document.setAuthId(auth.getAuthId());
        }
         return document;
    }
}
