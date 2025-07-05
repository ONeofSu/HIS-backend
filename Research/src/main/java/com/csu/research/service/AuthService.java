package com.csu.research.service;

import com.csu.research.entity.Auth;
import com.csu.research.entity.Content;

public interface AuthService {
    Auth getAuthById(Long id);
    Auth getAuthByName(String name);

    boolean isQualifiedToSetCaptain(int userId,Long teamMemberId);

    boolean isQualifiedToAddContent(Content content,int userId);
    boolean isQualifiedToReadContent(Content content,int userId);
    boolean isQualifiedToReadContent(Long contentId,int userId);
    boolean isQualifiedToWriteContent(Content content,int userId);
    boolean isQualifiedToWriteContent(Long contentId,int userId);
}
