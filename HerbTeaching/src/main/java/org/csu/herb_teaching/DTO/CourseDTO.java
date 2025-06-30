package org.csu.herb_teaching.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class CourseDTO {
    private String courseName;
    private Date courseStartTime;
    private Date courseEndTime;
    private String courseDes;
    private int courseType;
    private int courseObject;
}
