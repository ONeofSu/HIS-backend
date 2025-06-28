package org.csu.herbinfo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("herb_link_category")
public class HerbLinkCategory {
    @TableId("hlc_id")
    private int id;
    @TableField("herb_id")
    private int herbId;
    @TableField("category_id")
    private int categoryId;
}
