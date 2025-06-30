package org.csu.hisuser.VO;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class UserVO {
    private int id;
    private String username;
    private String phone;
    private String email;
    private String avatarUrl;
    private String role;
}
