package org.csu.histraining.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("live_stream")
public class LiveStream {
    public static final int NOT_STARTED = 0;
    public static final int ACTIVE = 1;
    public static final int END = 2;

    @TableId(value = "live_stream_id",type = IdType.AUTO)
    private Long id;
    @TableField(value = "live_room_id")
    private Long roomId;
    private String streamId;  // 流ID
    @TableField(value = "live_stream_protocol")
    private String protocol;  // 协议类型：rtmp/hls/flv
    @TableField(value = "live_stream_bitrate")
    private Integer bitrate;  // 码率
    @TableField(value = "live_stream_resolution")
    private String resolution; // 分辨率
    @TableField(value = "live_stream_status")
    private Integer status;   // 0:未启动 1:活跃 2:已结束
    @TableField(value = "live_stream_creat_time")
    private LocalDateTime createdAt;
    @TableField(value = "live_stream_update_time")
    private LocalDateTime updatedAt;

}
