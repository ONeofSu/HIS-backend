package org.csu.herb_teaching.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseDTO {
    private int courseId;
    private String courseName;
    private String coverImageUrl;
    private int courseType;
    private int courseObject;
    private int teacherId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime courseStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime courseEndTime;

    private String courseDes;
}
