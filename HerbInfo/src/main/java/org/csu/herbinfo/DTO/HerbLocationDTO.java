package org.csu.herbinfo.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.csu.herbinfo.entity.HerbLocation;

@Data
public class HerbLocationDTO {
    private String name;
    private int count;
    private String district;
    private String street;
    private double longitude;
    private double latitude;
}
