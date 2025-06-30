package org.csu.herb_teaching.VO;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class CourseVO {
    private int courseId;
    private String courseName;
    private Date courseStartTime;
    private Date courseEndTime;
    private String courseDes;
    private int labCount;
    private String status;
    private int courseType;
    private String courseTypeName;
    private int courseObject;
    private String courseObjectName;
    private List<LabVO> recentLabs;
} 