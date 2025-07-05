package com.csu.research.service;

import com.csu.research.entity.Auth;
import com.csu.research.entity.Content;
import com.csu.research.entity.Document;
import com.csu.research.entity.Topic;

public interface AuthService {
    Auth getAuthById(Long id);
    Auth getAuthByName(String name);

    boolean isCaptainOfTheTeam(int userId,Long teamId);
    boolean isQualifiedToSetCaptain(int userId,Long teamMemberId);

    boolean isQualifiedToHandleTopic(Topic topic,int userId);
    boolean isQualifiedToHandleTopic(Long topicId,int userId);

    boolean isQualifiedToAddContent(Content content,int userId);
    boolean isQualifiedToReadContent(Content content,int userId);
    boolean isQualifiedToReadContent(Long contentId,int userId);
    boolean isQualifiedToWriteContent(Content content,int userId);
    boolean isQualifiedToWriteContent(Long contentId,int userId);

    boolean isQualifiedToAddDocument(Document document, int userId);
    boolean isQualifiedToWriteDocument(Long documentId,int userId);
    boolean isQualifiedToReadDocument(Long documentId,int userId);

}
