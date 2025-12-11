package org.csu.hisuser.DTO;

import lombok.Data;

@Data
public class BatchInviteDTO {
    private String schoolName;
    private String userName;
    private Integer inviteCodeType; // 1:学生 2:教师
}
