package org.csu.performance.VO;

import lombok.Data;

import java.util.List;

/**
 * 分页结果VO
 */
@Data
public class PageResultVO<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 数据列表
     */
    private List<T> list;

    public PageResultVO() {
    }

    public PageResultVO(Long total, Long pages, List<T> list) {
        this.total = total;
        this.pages = pages;
        this.list = list;
    }
} 