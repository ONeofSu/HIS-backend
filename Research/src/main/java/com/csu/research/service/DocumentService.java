package com.csu.research.service;

import com.csu.research.DTO.DocumentDTO;
import com.csu.research.entity.Document;
import com.csu.research.vo.DocumentVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DocumentService {
    Document addDocument(Document document);
    boolean deleteDocument(Long id);
    Document getDocumentById(Long id);
    boolean updateDocument(Document document);
    boolean isDocumentExist(Long id);
    List<Document> getAllDocumentsOfTopic(Long topicId);

    DocumentVo transferDocumentToVo(Document document);
    List<DocumentVo> transferDocumentsToVo(List<Document> documents);
    Document transferDTOToDocument(DocumentDTO documentDTO,int userId);
}
