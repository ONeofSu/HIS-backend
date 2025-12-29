package org.csu.herb_teaching.stub;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * HerbInfoFeignClient 的测试桩（Stub）。
 *
 * 使用内存 Map 维护药材信息，结构与实际 Feign 返回的 Map 类似：
 * - herbId
 * - herbName
 * - description
 * - category
 */
public class HerbInfoFeignClientStub {

    private final Map<Integer, Map<String, Object>> herbsById = new HashMap<>();
    private final Map<String, Map<String, Object>> herbsByName = new HashMap<>();

    public void clearAll() {
        herbsById.clear();
        herbsByName.clear();
    }

    public void addHerb(int herbId, String herbName, String description, String category) {
        Map<String, Object> herb = new HashMap<>();
        herb.put("herbId", herbId);
        herb.put("herbName", herbName);
        herb.put("description", description);
        herb.put("category", category);

        herbsById.put(herbId, herb);
        herbsByName.put(herbName, herb);
    }

    public Map<String, Object> getHerbInfoById(int herbId) {
        return herbsById.get(herbId);
    }

    public Map<String, Object> getHerbInfoByName(String herbName) {
        return herbsByName.get(herbName);
    }

    public Map<String, Object> getAllHerbs() {
        // 模拟 HerbInfoFeignClient#getAllHerbs 的返回结构，外层再包一层 data
        Map<String, Object> result = new HashMap<>();
        result.put("data", Collections.unmodifiableCollection(herbsById.values()));
        return result;
    }
}


