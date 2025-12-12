package org.csu.herbinfo.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.csu.herbinfo.entity.HerbCategory;
import org.csu.herbinfo.entity.HerbLinkCategory;

import java.util.List;

@Data
public class HerbVO {
    private int id;
    private String name;
    private String origin;
    private String image;
    private String des;
    private List<HerbLinkCategoryVO> herbLinkCategoryList;
}
