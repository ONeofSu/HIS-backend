package org.csu.histraining.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.csu.histraining.entity.LiveRoom;

import java.time.LocalDateTime;

@Data
public class LiveRoomDTO {
    private String title;
    private String coverUrl;
}
