package org.csu.herb_teaching.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CourseHerbDTO {
    private int courseId;
    private List<Integer> herbIds;
} 