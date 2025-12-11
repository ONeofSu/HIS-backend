package com.csu.research.DTO;

import lombok.Data;

@Data
public class ContentDTO {
    private Long topicId;
    private String contentType;
    private String auth;
    private String contentName;
    private String contentDes;
}
