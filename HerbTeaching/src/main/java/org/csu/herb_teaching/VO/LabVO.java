package org.csu.herb_teaching.VO;

import lombok.Data;
import java.util.Date;

@Data
public class LabVO {
    private int labId;
    private int courseId;
    private String courseName;
    private int userId;
    private String userName;
    private Date labSubmitTime;
    private Date labEndTime;
    private String labDes;
    private int resourceCount;
    private HerbVO herbInfo;
    private LabInfoVO labInfo;
} 