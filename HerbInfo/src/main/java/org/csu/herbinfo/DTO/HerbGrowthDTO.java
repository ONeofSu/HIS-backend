package org.csu.herbinfo.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.csu.herbinfo.entity.HerbGrowth;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Data
public class HerbGrowthDTO {
    private String herbName;
    private String batchCode;
    private double wet;
    private double temperature;
    private String des;
    private double longitude;
    private double latitude;
    private int userId;
    private Timestamp recordTime;
    private String imgUrl;
}
