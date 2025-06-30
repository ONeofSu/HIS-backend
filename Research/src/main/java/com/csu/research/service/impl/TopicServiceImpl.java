package com.csu.research.service.impl;

import com.csu.research.entity.Team;
import com.csu.research.entity.Topic;
import com.csu.research.mapper.TeamMapper;
import com.csu.research.mapper.TopicMapper;
import com.csu.research.service.TopicService;
import com.csu.research.vo.TopicVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private TeamMapper teamMapper;
    @Override
    public TopicVo findTopicByTopicId(Long topicId) throws Exception {
        Topic topic = topicMapper.selectById(topicId);
        if (topic == null) {
            throw new Exception("not found");
        }
        log.info("topic.id: {}", topic.getTopicId());
        Team team = teamMapper.selectById(topic.getTeamId());
        TopicVo topicVo = new TopicVo();
        topicVo.setTopicId(topic.getTopicId());
        topicVo.setTopicName(topic.getTopicName());
        topicVo.setTeamId(topic.getTeamId());
        topicVo.setTopicDes(topic.getTopicDes());
        topicVo.setTopicStartTime(topic.getTopicStartTime());
        topicVo.setTopicEndTime(topic.getTopicEndTime());
        topicVo.setTopicStatus(topic.getTopicStatus());
        topicVo.setTeamName(team.getTeamName());

        return topicVo;
    }

    @Override
    public boolean updateTopic(Topic topic) {
        int i = topicMapper.updateById(topic);
        return i == 1;
    }

    @Override
    public List<TopicVo> searchTopic(String query) {
        return topicMapper.searchTopic(query);
    }

    @Override
    public Boolean addTopic(Topic topic) {
        return topicMapper.insert(topic) == 1;
    }

    @Override
    public Boolean delTopic(Long topicId) {
        return topicMapper.deleteById(topicId) == 1;
    }
}
