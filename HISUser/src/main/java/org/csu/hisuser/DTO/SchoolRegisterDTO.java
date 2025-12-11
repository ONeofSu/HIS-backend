package org.csu.hisuser.DTO;

import lombok.Data;

@Data
public class SchoolRegisterDTO extends RegisterDTO {
    private String schoolName;
    private String userName;
    private String invitationCode;
}
