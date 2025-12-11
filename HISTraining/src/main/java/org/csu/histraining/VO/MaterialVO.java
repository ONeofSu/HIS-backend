package org.csu.histraining.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.csu.histraining.entity.Content;
import org.csu.histraining.entity.Material;

import java.sql.Timestamp;
import java.util.List;

@Data
public class MaterialVO{
    private int id;
    private String title;
    private String type;
    private String des;
    private int herbId;
    private String herbName;
    private int userId;
    private String userName;
    private Timestamp time;
    private int count;

    private List<ContentVO> contents;
}
