package org.csu.herb_teaching.VO;

import lombok.Data;
import java.util.List;

@Data
public class PageVO<T> {
    private long total;
    private long pages;
    private List<T> list;
} 