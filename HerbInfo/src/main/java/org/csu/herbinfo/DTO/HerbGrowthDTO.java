package org.csu.herbinfo.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.csu.herbinfo.entity.HerbGrowth;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

@Data
public class HerbGrowthDTO {
    private int id;
    private int herbId;
    private String batchCode;
    private double wet;
    private double temperature;
    private String des;
    private double longitude;
    private double latitude;
    private int userId;
    private Timestamp recordTime;
    private MultipartFile img;

    public HerbGrowth transferIntoHerbGrowthExceptImg() {
        HerbGrowth hg = new HerbGrowth();
        hg.setHerbId(this.herbId);
        hg.setBatchCode(this.batchCode);
        hg.setWet(this.wet);
        hg.setTemperature(this.temperature);
        hg.setDes(this.des);
        hg.setLongitude(this.longitude);
        hg.setLatitude(this.latitude);
        hg.setUserId(this.userId);
        hg.setRecordTime(this.recordTime);
        return hg;
    }
}
