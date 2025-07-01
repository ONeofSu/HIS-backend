package org.csu.herb_teaching.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.csu.herb_teaching.entity.CourseResource;
import org.csu.herb_teaching.entity.Lab;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CourseDetailVO {
    private int courseId;
    private String courseName;
    private String coverImageUrl;
    private int courseType;
    private int courseObject;
    private int teacherId;
    private String teacherName;
    private String teacherAvatar;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime courseStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime courseEndTime;
    
    private String courseDes;
    private BigDecimal courseAverageRating;
    private int courseRatingCount;

    private List<Lab> labs;
    private List<CourseResource> resources;
    private List<HerbSimpleVO> herbs;
}
