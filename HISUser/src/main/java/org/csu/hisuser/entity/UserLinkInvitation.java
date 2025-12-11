package org.csu.hisuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_link_invitation")
public class UserLinkInvitation {
    @TableId(value = "uli_id",type= IdType.AUTO)
    private Long id;
    private int userId;
    private Long codeId;
}
