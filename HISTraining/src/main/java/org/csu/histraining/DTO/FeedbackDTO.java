package org.csu.histraining.DTO;

import lombok.Data;

@Data
public class FeedbackDTO {
    private int materialId;
    private String content;
    private int rating;
}
