package org.csu.herb_teaching.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CourseResourceDTO {
    @JsonProperty("resourceType")
    private int courseResourceType;

    @JsonProperty("resourceOrder")
    private int courseResourceOrder;
    
    @JsonProperty("resourceTitle")
    private String courseResourceTitle;
    
    @JsonProperty("contentUrl")
    private String courseResourceContent;

    @JsonProperty("metadata")
    private String courseResourceMetadata;

    private int courseResourceId;
    private int courseId;
} 