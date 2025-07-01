package org.csu.herb_teaching.DTO;

import lombok.Data;

@Data
public class LabDTO {
    private String labName;
    private String labSteps;
    private int labOrder;
    private int labId;
    private int courseId;
} 