package org.csu.herb_teaching.DTO;

import lombok.Data;

@Data
public class LabResourceDTO {
    private Integer labResourceType;
    private Integer labResourceOrder;
    private String labResourceTitle;
    private String labResourceMetadata;
} 