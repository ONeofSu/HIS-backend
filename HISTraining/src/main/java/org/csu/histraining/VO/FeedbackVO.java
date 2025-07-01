package org.csu.histraining.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class FeedbackVO {
    private int id;
    private int materialId;
    private int userId;
    private String userName;
    private String content;
    private Timestamp time;
    private int rating;
}
