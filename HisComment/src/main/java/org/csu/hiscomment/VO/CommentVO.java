package org.csu.hiscomment.VO;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class CommentVO {
    private int commentId;
    private int userId;
    private String userName;
    private String userAvatar;
    private String content;
    private int parentId;
    private int rootId;
    private int likeCount;
    private Date createTime;
    private boolean isMine;
    private boolean isLiked;
    private List<CommentVO> children;
    
    // 敏感词过滤相关字段
    private boolean isFiltered;        // 是否被过滤过
    private int filterLevel;           // 过滤级别（0-无，1-轻度，2-中度，3-重度）
    private String sensitiveWords;     // 检测到的敏感词
    private String sensitiveTypes;     // 敏感词类型
}