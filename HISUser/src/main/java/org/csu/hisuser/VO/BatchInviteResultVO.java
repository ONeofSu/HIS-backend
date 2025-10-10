package org.csu.hisuser.VO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BatchInviteResultVO {
    private String name;
    private String schoolName;
    private int category;
    private boolean success;
    private String message;
    private String inviteCode; // 成功时返回的邀请码
}
