package org.csu.evaluationanddeclaration.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("evaluation_indicator")
public class EvaluationIndicator {

    @TableId(value = "indicator_id", type = IdType.AUTO)
    private Long indicatorId;

    @TableField("indicator_name")
    private String indicatorName;

    @TableField("weight")
    private Double weight;
}
