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

    private String courseTypeName;
    private String courseObjectName;

    public String getCourseTypeName() {
        switch (courseType) {
            case 0: return "选修";
            case 1: return "必修";
            default: return "未知";
        }
    }
    public String getCourseObjectName() {
        switch (courseObject) {
            case 0: return "本科生";
            case 1: return "研究生";
            case 2: return "博士生";
            case 3: return "其他专业人士";
            default: return "未知";
        }
    }
}
