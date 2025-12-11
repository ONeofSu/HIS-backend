package org.csu.evaluationanddeclaration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("herb_rating_detail")
public class HerbRatingDetail {

    @TableId(value = "herb_evaluation_detail_id", type = IdType.AUTO)
    private Long herbEvaluationDetailId;

    @TableField("herb_evaluation_id")
    private Long herbEvaluationId;

    @TableField("herb_id")
    private Long herbId;

    @TableField("indicator_id")
    private Long indicatorId;

    @TableField("avg_score")
    private Double avgScore;
}