package org.csu.herb_teaching.VO;

import lombok.Data;

@Data
public class UserVO {
    private int id;
    private String username;
    private String avatarUrl;
    // 可根据需要扩展其它字段
} 