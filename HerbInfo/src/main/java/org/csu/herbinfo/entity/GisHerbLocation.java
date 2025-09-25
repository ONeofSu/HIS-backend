package org.csu.herbinfo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.csu.herbinfo.handler.GeometryTypeHandler;
import org.locationtech.jts.geom.Point;

@Data
@TableName("herb_location")
public class GisHerbLocation {
    @TableId(value = "location_id", type = IdType.AUTO)
    private Long id;
    private int herbId;
    @TableField("location_count")
    private Integer count;
    private int districtId;
    private int streetId;
    @TableField(value = "geom", typeHandler = GeometryTypeHandler.class)
    private Point geom;
}
