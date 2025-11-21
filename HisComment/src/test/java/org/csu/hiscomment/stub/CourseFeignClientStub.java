package org.csu.hiscomment.stub;

import org.csu.hiscomment.feign.CourseFeignClient;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * CourseFeignClient 的 Stub 实现
 * 用于桩集成测试，模拟课程服务的行为
 * 
 * 注意：这是一个测试用的 Stub 实现，不用于生产环境
 */
@Component
public class CourseFeignClientStub implements CourseFeignClient {

    // 模拟课程数据存储（只存储课程ID，表示课程存在）
    private final Set<Integer> courseDatabase = new HashSet<>();

    public CourseFeignClientStub() {
        // 初始化测试数据
        initTestData();
    }

    private void initTestData() {
        // 初始化课程数据
        courseDatabase.add(1);
        courseDatabase.add(2);
        courseDatabase.add(3);
    }

    @Override
    public boolean isCourseExist(int courseId) {
        return courseDatabase.contains(courseId);
    }

    // Stub 特有的辅助方法，用于测试时设置数据
    public void addCourse(int courseId) {
        courseDatabase.add(courseId);
    }

    public void removeCourse(int courseId) {
        courseDatabase.remove(courseId);
    }

    public void clearAll() {
        courseDatabase.clear();
        initTestData();
    }
}


