package org.csu.hisuser.DTO;

import lombok.Data;

@Data
public class ResetPswDTO {
    private String username;
    private String oldPassword;
    private String newPassword;
}
