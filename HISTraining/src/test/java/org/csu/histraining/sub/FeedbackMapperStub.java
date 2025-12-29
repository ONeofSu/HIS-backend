package org.csu.histraining.sub;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.session.ResultHandler;
import org.csu.histraining.entity.Feedback;
import org.csu.histraining.mapper.FeedbackMapper;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * FeedbackMapper 的桩实现
 * 使用内存存储模拟数据库操作
 */
public class FeedbackMapperStub implements FeedbackMapper {

    // 使用内存 Map 模拟数据库表
    private final Map<Integer, Feedback> storage = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public int insert(Feedback entity) {
        int id = idGenerator.getAndIncrement();
        entity.setId(id);
        storage.put(id, entity);
        return 1; // 返回影响行数
    }

    @Override
    public int deleteById(Feedback entity) {
        return 0;
    }

    @Override
    public int delete(Wrapper<Feedback> queryWrapper) {
        return 0;
    }

    public Feedback selectById(Integer id) {
        return storage.get(id);
    }

    @Override
    public List<Feedback> selectList(Wrapper<Feedback> queryWrapper) {
        if (queryWrapper == null) {
            return new ArrayList<>(storage.values());
        }

        // 简化处理：根据 userId 过滤
        // 实际应解析 queryWrapper 的条件
        return new ArrayList<>(storage.values());
    }

    @Override
    public void selectList(Wrapper<Feedback> queryWrapper, ResultHandler<Feedback> resultHandler) {

    }

    @Override
    public List<Feedback> selectList(IPage<Feedback> page, Wrapper<Feedback> queryWrapper) {
        return List.of();
    }

    @Override
    public void selectList(IPage<Feedback> page, Wrapper<Feedback> queryWrapper, ResultHandler<Feedback> resultHandler) {

    }

    @Override
    public List<Map<String, Object>> selectMaps(Wrapper<Feedback> queryWrapper) {
        return List.of();
    }

    @Override
    public void selectMaps(Wrapper<Feedback> queryWrapper, ResultHandler<Map<String, Object>> resultHandler) {

    }

    @Override
    public List<Map<String, Object>> selectMaps(IPage<? extends Map<String, Object>> page, Wrapper<Feedback> queryWrapper) {
        return List.of();
    }

    @Override
    public void selectMaps(IPage<? extends Map<String, Object>> page, Wrapper<Feedback> queryWrapper, ResultHandler<Map<String, Object>> resultHandler) {

    }

    @Override
    public <E> List<E> selectObjs(Wrapper<Feedback> queryWrapper) {
        return List.of();
    }

    @Override
    public <E> void selectObjs(Wrapper<Feedback> queryWrapper, ResultHandler<E> resultHandler) {

    }

    public int deleteById(Integer id) {
        return storage.remove(id) != null ? 1 : 0;
    }

    @Override
    public int updateById(Feedback entity) {
        if (storage.containsKey(entity.getId())) {
            storage.put(entity.getId(), entity);
            return 1;
        }
        return 0;
    }

    @Override
    public int update(Feedback entity, Wrapper<Feedback> updateWrapper) {
        return 0;
    }

    @Override
    public Feedback selectById(Serializable id) {
        return null;
    }

    @Override
    public List<Feedback> selectBatchIds(Collection<? extends Serializable> idList) {
        return List.of();
    }

    @Override
    public void selectBatchIds(Collection<? extends Serializable> idList, ResultHandler<Feedback> resultHandler) {

    }

    @Override
    public Long selectCount(Wrapper<Feedback> queryWrapper) {
        return 0L;
    }

    // 其他 BaseMapper 方法根据需要实现或抛出异常
}
