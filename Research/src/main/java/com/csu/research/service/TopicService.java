package com.csu.research.service;

import com.csu.research.entity.Topic;
import com.csu.research.vo.TopicVo;

import java.util.List;

public interface TopicService {

    /**
     * 根据topicId获取一个研究课题的信息
     * @param topicId Long
     * @return TopicVo
     */
    TopicVo findTopicByTopicId(Long topicId) throws Exception;

    /**
     * 更新研究课题
     * @param topic Topic
     * @return boolean
     */
    boolean updateTopic(Topic topic);

    /**
     * 搜索课题
     * @param query String
     * @return List<TopicVo>
     */
    List<TopicVo> searchTopic(String query);

    /**
     * 添加课题
     * @author Panhao Zhou
     * @param topic Topic
     * @return Boolean
     */
    Boolean addTopic(Topic topic);

    /**
     * 删除课题
     * @author Panhao Zhou
     * @param topicId Long
     * @return Boolean
     */
    Boolean delTopic(Long topicId);
}
