package org.csu.herb_teaching.config;

import org.csu.herb_teaching.stub.HerbInfoFeignClientStub;
import org.csu.herb_teaching.stub.UserFeignClientStub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 测试用 Stub Bean 配置。
 *
 * 在集成测试中通过 @Import(StubTestConfiguration.class) 引入，
 * 提供可注入的 UserFeignClientStub / HerbInfoFeignClientStub，
 * 供测试代码在内存中预置用户、药材等数据。
 */
@Configuration
public class StubTestConfiguration {

    @Bean
    public UserFeignClientStub userFeignClientStub() {
        return new UserFeignClientStub();
    }

    @Bean
    public HerbInfoFeignClientStub herbInfoFeignClientStub() {
        return new HerbInfoFeignClientStub();
    }
}


