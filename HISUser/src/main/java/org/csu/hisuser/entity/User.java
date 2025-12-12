package org.csu.hisuser.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId("user_id")
    private int id;
    private String username;
    private String password;
    private String phone;
    private String email;
    @TableField(value = "avatar_url")
    private String avatarUrl;
}
