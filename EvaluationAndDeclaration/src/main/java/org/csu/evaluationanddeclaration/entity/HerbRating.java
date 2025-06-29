package org.csu.evaluationanddeclaration.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("herb_rating")
public class HerbRating {

    @TableId(value = "herb_evaluation_id", type = IdType.AUTO)
    private Long herbEvaluationId;

    @TableField("herb_id")
    private Long herbId;

    @TableField("total_score")
    private Double totalScore;
}

