package org.csu.herb_teaching.VO;

import lombok.Data;

@Data
public class HerbVO {
    private int herbId;
    private String herbName;
    private String origin;
    private String image;
    private int relatedLabCount;
} 