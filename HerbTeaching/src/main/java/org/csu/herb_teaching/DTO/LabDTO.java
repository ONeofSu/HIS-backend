package org.csu.herb_teaching.DTO;

import lombok.Data;
import java.util.Date;

@Data
public class LabDTO {
    private int courseId;
    private int userId;
    private Date labEndTime;
    private String labDes;
    private int herbId;
    private String labInfoGoal;
    private String labInfoSummary;
} 