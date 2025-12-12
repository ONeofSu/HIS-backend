package org.csu.histraining.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("live_room")
public class LiveRoom {
    public static final int NOT_LIVE = 0;
    public static final int LIVE = 1;
    public static final int LIVE_END = 2;

    @TableId(value = "live_room_id",type = IdType.AUTO)
    private Long id;
    @TableField(value = "live_room_title")
    private String title;
    @TableField(value = "live_room_cover_url")
    private String coverUrl;
    private int userId;
    private String streamKey;  // 推流密钥
    private String streamUrl;  // 推流地址
    private String hlsUrl;     // HLS播放地址
    private String flvUrl;     // HTTP-FLV播放地址
    @TableField(value = "live_room_status")
    private Integer status;    // 0:未开播 1:直播中 2:直播结束
    @TableField(value = "live_room_view_count")
    private Long viewCount;    // 观看人数
    @TableField(value = "live_room_like_count")
    private Long likeCount;    // 点赞数

    @TableField("live_room_start_time")
    private LocalDateTime startTime;  // 开播时间
    @TableField("live_room_end_time")
    private LocalDateTime endTime;    // 结束时间
    @TableField("live_room_creat_time")
    private LocalDateTime createdAt;
    @TableField("live_room_update_time")
    private LocalDateTime updatedAt;
}
