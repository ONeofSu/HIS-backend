package org.csu.herb_teaching.VO;

import lombok.Data;

@Data
public class LabInfoVO {
    private int labInfoId;
    private int labId;
    private String labDes;
    private String courseName;
    private int herbId;
    private String herbName;
    private String labInfoGoal;
    private String labInfoSummary;
    private Boolean labInfoIsvalid;
} 