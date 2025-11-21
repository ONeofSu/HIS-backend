package org.csu.hiscomment.stub;

import org.csu.hiscomment.feign.HerbFeignClient;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * HerbFeignClient 的 Stub 实现
 * 用于桩集成测试，模拟中草药服务的行为
 * 
 * 注意：这是一个测试用的 Stub 实现，不用于生产环境
 */
@Component
public class HerbFeignClientStub implements HerbFeignClient {

    // 模拟中草药数据存储（只存储中草药ID，表示中草药存在）
    private final Set<Integer> herbDatabase = new HashSet<>();

    public HerbFeignClientStub() {
        // 初始化测试数据
        initTestData();
    }

    private void initTestData() {
        // 初始化中草药数据
        herbDatabase.add(1);
        herbDatabase.add(2);
        herbDatabase.add(3);
    }

    @Override
    public boolean isHerbExist(int herbId) {
        return herbDatabase.contains(herbId);
    }

    // Stub 特有的辅助方法，用于测试时设置数据
    public void addHerb(int herbId) {
        herbDatabase.add(herbId);
    }

    public void removeHerb(int herbId) {
        herbDatabase.remove(herbId);
    }

    public void clearAll() {
        herbDatabase.clear();
        initTestData();
    }
}


