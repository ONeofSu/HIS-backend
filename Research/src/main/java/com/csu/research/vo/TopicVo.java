package com.csu.research.vo;

import com.csu.research.entity.Topic;
import lombok.Data;

@Data
public class TopicVo extends Topic {
    private String teamName;
}
