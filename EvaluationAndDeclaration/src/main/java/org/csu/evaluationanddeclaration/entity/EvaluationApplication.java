package org.csu.evaluationanddeclaration.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("evaluation_application")
public class EvaluationApplication {

    @TableId(value = "application_id", type = IdType.AUTO)
    private Long applicationId;

    @TableField("evaluation_id")
    private Long evaluationId;

    @TableField("application_type")
    private Long applicationType;

    @TableField("application_state")
    private String applicationState;

    @TableField("user_id")
    private Long userId;
}

