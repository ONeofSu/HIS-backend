package org.csu.herbinfo.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.csu.herbinfo.entity.HerbGrowth;

import java.sql.Timestamp;

@Data
public class HerbGrowthVO {
    private int id;
    private int herbId;
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
