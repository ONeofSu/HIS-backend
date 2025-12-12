package org.csu.hisuser.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_category")
public class UserCategory {
    @TableId("category_id")
    private int id;
    @TableField("category_name")
    private String name;
}
