package org.csu.hisuser.DTO;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private String username;
    private String email;
}
