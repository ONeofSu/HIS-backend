package org.csu.evaluationanddeclaration.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("herb_evaluation")
public class HerbEvaluation {

    @TableId(value = "evaluation_id", type = IdType.AUTO)
    private Long evaluationId;

    @TableField("herb_id")
    private Long herbId;

    @TableField("user_id")
    private Long userId;

    @TableField("evaluation_state")
    private String evaluationState;

    @TableField("auditor_user_id")
    private Long auditorId;

    @TableField("total_score")
    private Float totalScore;

    @TableField(value = "evaluate_time", fill = FieldFill.INSERT)
    private Date evaluateTime;

    public void formatTime(Date date){
        evaluateTime = date;
    }


}

