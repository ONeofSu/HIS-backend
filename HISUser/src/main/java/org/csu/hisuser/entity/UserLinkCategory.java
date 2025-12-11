package org.csu.hisuser.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_link_category")
public class UserLinkCategory {
    @TableId("ulc_id")
    private int ulcId;
    @TableField("user_id")
    private int userId;
    @TableField("category_id")
    private int categoryId;
}
