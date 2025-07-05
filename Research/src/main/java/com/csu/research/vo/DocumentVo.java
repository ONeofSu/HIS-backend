package com.csu.research.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentVo {
    private Long documentId;

    private Long topicId;
    private String topicName;

    private int userId;

    private String documentName;

    private String documentDes;

    private String documentType;

    private String documentUrl;

    private LocalDateTime documentTime;
}
