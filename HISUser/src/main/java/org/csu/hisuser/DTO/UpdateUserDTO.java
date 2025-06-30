package org.csu.hisuser.DTO;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private int userId;
    private String phone;
    private String email;
    private String avatarUrl;
}
