package com.csu.research.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("auth")
public class Auth {

    @TableId(value="auth_id", type= IdType.AUTO)
    private Long authId;

    @TableField("auth_name")
    private String authName;
}
