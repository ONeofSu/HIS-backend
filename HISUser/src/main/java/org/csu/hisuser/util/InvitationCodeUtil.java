package org.csu.hisuser.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InvitationCodeUtil {
    public static String generateInvitationCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase(); //生成邀请码
    }
}
