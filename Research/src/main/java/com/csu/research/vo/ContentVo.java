package com.csu.research.vo;

import com.csu.research.entity.ContentBlock;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContentVo {
    private Long contentId;
    private String contentName;
    private String contentDes;
    private LocalDateTime contentTime;

    private Long topicId;
    private String topicName;

    private Long contentTypeId;
    private String contentTypeName;

    private Short contentIsValid;

    private List<ContentBlock> contentBlocks;

    private Long userId;
}
