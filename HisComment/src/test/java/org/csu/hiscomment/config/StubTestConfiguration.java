package org.csu.hiscomment.config;

import org.csu.hiscomment.stub.CourseFeignClientStub;
import org.csu.hiscomment.stub.HerbFeignClientStub;
import org.csu.hiscomment.stub.UserFeignClientStub;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Stub 测试配置类
 * 用于在测试中提供 Stub 实现实例
 * 
 * 注意：由于 Feign 客户端是接口，不能直接替换。
 * 在测试中使用 @MockBean 模拟 Feign 客户端，然后使用 Stub 类来设置 Mock 行为。
 */
@TestConfiguration
public class StubTestConfiguration {

    @Bean
    public UserFeignClientStub userFeignClientStub() {
        return new UserFeignClientStub();
    }

    @Bean
    public CourseFeignClientStub courseFeignClientStub() {
        return new CourseFeignClientStub();
    }

    @Bean
    public HerbFeignClientStub herbFeignClientStub() {
        return new HerbFeignClientStub();
    }
}

