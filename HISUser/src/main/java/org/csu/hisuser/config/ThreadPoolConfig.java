package org.csu.hisuser.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {
    @Bean("inviteCodeExecutor")
    public ThreadPoolExecutor inviteCodeExecutor() {
        return new ThreadPoolExecutor(
                8,
                16,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1000),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
