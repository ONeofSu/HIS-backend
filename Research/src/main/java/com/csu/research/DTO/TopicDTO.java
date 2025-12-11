package com.csu.research.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TopicDTO {
    private String name;
    private Long teamId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String des;
    private String status;
}
