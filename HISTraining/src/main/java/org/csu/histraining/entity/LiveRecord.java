package org.csu.histraining.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("live_record")
public class LiveRecord {
    public static final int RECORDING = 0;
    public static final int RECORD_FINISH = 1;
    public static final int RECORD_HANDLING = 2;
    public static final int RECORD_USABLE = 3;
    public static final int RECORD_DELETED = 4;

    @TableId(value = "live_record_id",type = IdType.AUTO)
    private Long id;
    @TableField(value = "live_room_id")
    private Long roomId;
    @TableField(value = "live_record_file_name")
    private String fileName;
    @TableField(value = "live_record_file_url")
    private String fileUrl;
    @TableField(value = "live_record_file_size")
    private Long fileSize;
    @TableField(value = "live_record_duration")
    private Integer duration;  // 时长，单位秒
    @TableField(value = "live_record_start_time")
    private LocalDateTime startTime;
    @TableField(value = "live_record_end_time")
    private LocalDateTime endTime;
    @TableField(value = "live_record_status")
    private Integer status;   // 0:录制中 1:录制完成 2:处理中 3:可用 4:删除
    @TableField(value = "live_record_creat_time")
    private LocalDateTime createdAt;
    @TableField(value = "live_record_update_time")
    private LocalDateTime updatedAt;

}
