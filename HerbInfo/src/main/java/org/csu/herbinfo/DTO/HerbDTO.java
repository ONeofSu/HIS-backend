package org.csu.herbinfo.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class HerbDTO {
    private String name;
    private String origin;
    private String img_url;
    private String des;
}
