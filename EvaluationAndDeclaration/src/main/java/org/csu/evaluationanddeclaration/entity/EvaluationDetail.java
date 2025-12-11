package org.csu.evaluationanddeclaration.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("evaluation_detail")
public class EvaluationDetail {

    @TableId(value = "evaluation_detail_id", type = IdType.AUTO)
    private Long evaluationDetailId;

    @TableField("evaluation_id")
    private Long evaluationId;

    @TableField("indicator_id")
    private Long indicatorId;

    @TableField("indicator_score")
    private Double indicatorScore;

    @TableField("comment")
    private String comment;

    @TableField("material")
    private String material;
}

