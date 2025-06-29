package org.csu.herbinfo.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class StreetVO {
    private int streetId;
    private int districtId;
    private String streetName;
    private String districtName;
}
