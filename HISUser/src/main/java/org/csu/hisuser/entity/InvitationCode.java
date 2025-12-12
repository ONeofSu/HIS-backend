package org.csu.hisuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("invitation_code")
@Data
public class InvitationCode {
    @TableId(value = "code_id",type = IdType.AUTO)
    private Long codeId;
    private String code;
    private int categoryId;
    @TableField(value = "user_id")
    private int createUserId;
    private String inviteSchool;
    private String inviteName;
    private Boolean codeIsUsed;
    private LocalDateTime codeExpireTime;
}
