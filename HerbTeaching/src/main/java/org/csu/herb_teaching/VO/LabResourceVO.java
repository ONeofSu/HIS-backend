package org.csu.herb_teaching.VO;

import lombok.Data;
import java.util.Date;

@Data
public class LabResourceVO {
    private int labResourceId;
    private int labId;
    private LabInfoVO labInfo;
    private int labResourceType;
    private String labResourceTypeName;
    private int labResourceOrder;
    private String labResourceTitle;
    private String labResourceContent;
    private String labResourceMetadata;
    private Date labResourceTime;
    private Boolean labResourceIsvalid;
    private String fileSize;
    private String previewUrl;
} 