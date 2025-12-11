package org.csu.hiscomment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("sensitive_words")
public class SensitiveWord {
    @TableId(type = IdType.AUTO)
    private int sensitiveId;
    private String sensitiveWord;
    private String sensitiveWordType;
    private int sensitiveLevel;
    private Integer sensitiveStatus;
    private Date createTime;
    private int createBy;
} 