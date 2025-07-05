package com.csu.research.DTO;

import lombok.Data;

@Data
public class DocumentDTO {
    private Long topicId;
    private String auth;
    private String documentName;
    private String documentType;
    private String documentDes;
    private String documentUrl;
}
