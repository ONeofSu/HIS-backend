package org.csu.histraining.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.csu.histraining.entity.Content;

@Data
public class ContentVO{
    private int id;
    private String type;
    private int order;
    private String des;
    private String url;
}
