package org.csu.herbinfo.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class HerbLinkCategoryVO {
    private int id;
    private int herbId;
    private String herbName;
    private int categoryId;
    private String categoryName;
}
