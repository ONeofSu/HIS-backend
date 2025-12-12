package org.csu.herbinfo.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class HerbLocationVO {
    private int id;
    private int herbId;
    private String herbName;
    private int count;
    private int districtId;
    private String districtName;
    private int streetId;
    private String streetName;
    private double longitude;
    private double latitude;
}
