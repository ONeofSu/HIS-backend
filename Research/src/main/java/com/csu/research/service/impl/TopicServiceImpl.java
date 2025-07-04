package com.csu.research.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.research.DTO.TopicDTO;
import com.csu.research.entity.Team;
import com.csu.research.entity.Topic;
import com.csu.research.mapper.TeamMapper;
import com.csu.research.mapper.TopicMapper;
import com.csu.research.service.TopicService;
import com.csu.research.service.UserService;
import com.csu.research.vo.TopicVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private TeamMapper teamMapper;
    @Override
    public TopicVo findTopicByTopicId(Long topicId) throws Exception {
        Topic topic = topicMapper.selectById(topicId);
        if (topic == null) {
            throw new Exception("not found");
        }
        log.info("topic.id: {}", topic.getTopicId());

        return transferTopicToVO(topic);
    }

    @Override
    public boolean updateTopic(Topic topic) {
        Topic oir = topicMapper.selectById(topic.getTopicId());
        if (oir == null || !oir.isTopicIsvalid()){
            return false;
        }
        int i = topicMapper.updateById(topic);
        return i == 1;
    }

    @Override
    public List<TopicVo> searchTopic(String query) {
        return topicMapper.searchTopic(query);
    }

    @Override
    public Topic addTopic(Topic topic) {
        topic.setTopicIsvalid(true);
        topicMapper.insert(topic);
        return topic;
    }

    @Override
    public Boolean delTopic(Long topicId) {
        Topic topic = topicMapper.selectById(topicId);
        if (topic == null || !topic.isTopicIsvalid()) {
            return false;
        }
        topic.setTopicIsvalid(false);
        topicMapper.updateById(topic);
        return true;
    }

    @Override
    public List<Topic> getTopicsByUserId(int userId,int page,int size) {
        Page<Topic> pageParam = new Page<>(page,size);
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("topic_isvalid", true);
        return topicMapper.selectPage(pageParam,queryWrapper).getRecords();
    }

    @Override
    public List<Topic> getAllTopics(int page, int size) {
        Page<Topic> pageParam = new Page<>(page,size);
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("topic_isvalid", true);
        return topicMapper.selectPage(pageParam,queryWrapper).getRecords();
    }

    @Override
    public TopicVo transferTopicToVO(Topic topic) {
        TopicVo topicVo = new TopicVo();
        topicVo.setTopicId(topic.getTopicId());
        topicVo.setTopicName(topic.getTopicName());
        topicVo.setTeamId(topic.getTeamId());
        topicVo.setTopicDes(topic.getTopicDes());
        topicVo.setTopicStartTime(topic.getTopicStartTime());
        topicVo.setTopicEndTime(topic.getTopicEndTime());
        topicVo.setTopicStatus(topic.getTopicStatus());
        topicVo.setTopicIsvalid(topic.isTopicIsvalid());

        switch(topic.getTopicStatus()){
            case 0:{
                topicVo.setStatusName("立项中");
                break;
            }
            case 1:{
                topicVo.setStatusName("进行中");
                break;
            }
            case 2:{
                topicVo.setStatusName("已结题");
                break;
            }
            default:{
                topicVo.setTopicName("error");
                break;
            }
        }

        Team team = teamMapper.selectById(topic.getTeamId());
        topicVo.setTeamName(team.getTeamName());

        return topicVo;
    }

    @Override
    public List<TopicVo> transferTopicToVO(List<Topic> topics) {
        List<TopicVo> topicVos = new ArrayList<>();
        for (Topic topic : topics) {
            TopicVo topicVo = transferTopicToVO(topic);
            topicVos.add(topicVo);
        }
        return topicVos;
    }

    @Override
    public Topic transferDTOToTopic(TopicDTO topicDTO) {
        Topic topic = new Topic();
        topic.setTopicName(topicDTO.getName());
        topic.setTeamId(topicDTO.getTeamId());
        topic.setTopicStartTime(topicDTO.getStartTime());
        topic.setTopicEndTime(topicDTO.getEndTime());
        topic.setTopicDes(topicDTO.getDes());
        topic.setTopicIsvalid(true);

        switch (topicDTO.getStatus()){
            case "立项中":{
                topic.setTopicStatus(0);
                break;
            }
            case "进行中":{
                topic.setTopicStatus(1);
                break;
            }
            case "已结题":{
                topic.setTopicStatus(2);
                break;
            }
        }

        return topic;
    }

    @Override
    public boolean isTopicExist(Long topicId) {
        Topic topic = topicMapper.selectById(topicId);
        return topic != null && topic.isTopicIsvalid();
    }

    @Override
    public boolean isTopicExist(String topicName) {
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("topic_name", topicName).eq("topic_isvalid", true);
        return topicMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public List<Topic> getAllTopicsByTeamId(Long teamId,int page, int size) {
        Page<Topic> pageParam = new Page<>(page,size);
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId).eq("topic_isvalid", true);
        return topicMapper.selectPage(pageParam,queryWrapper).getRecords();
    }

    @Override
    public List<Topic> getAllTopicsByTeamId(Long teamId) {
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId).eq("topic_isvalid", true);
        return topicMapper.selectList(queryWrapper);
    }

    @Override
    public Topic getTopicById(Long topicId) {
        Topic topic = topicMapper.selectById(topicId);
        if (topic == null || !topic.isTopicIsvalid()) {
            return null;
        }
        return topic;
    }
}
