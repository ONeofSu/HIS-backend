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
}