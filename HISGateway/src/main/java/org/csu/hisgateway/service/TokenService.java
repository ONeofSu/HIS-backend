package org.csu.hisgateway.service;

public interface TokenService {
    boolean isInBlackList(String token);
}
