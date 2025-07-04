package com.csu.research.service;

import com.csu.research.DTO.TopicDTO;
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
    Topic addTopic(Topic topic);

    /**
     * 删除课题
     * @author Panhao Zhou
     * @param topicId Long
     * @return Boolean
     */
    Boolean delTopic(Long topicId);

    /**
     * 获得Topic
     * @author ONeofSu
     * @param topicId Long
     * @return Topic
     */
    Topic getTopicById(Long topicId);

    /**
     * 根据userId获得Topic
     * @author ONeofSu
     * @param userId int
     * @param page int
     * @param size int
     * @return List<Topic>
     */
    List<Topic> getTopicsByUserId(int userId,int page,int size);

    /**
     * 获得所有Topic
     * @author ONeofSu
     * @param page int
     * @param size int
     * @return List<Topic>
     */
    List<Topic> getAllTopics(int page,int size);

    /**
     * 通过TeamId获得所有Topic
     * @author ONeofSu
     * @param page int
     * @param size int
     * @return List<Topic>
     */
    List<Topic> getAllTopicsByTeamId(Long teamId,int page,int size);
    List<Topic> getAllTopicsByTeamId(Long teamId);

    /**
     * 根据teamId获得Topic
     * @author ONeofSu
     * @param userId int
     * @param page int
     * @param size int
     * @return List<Topic>
     */

    /**
     * 转换Topic为VO
     * @author ONeofSu
     * @param topic Topic
     * @return TopicVo
     */
    TopicVo transferTopicToVO(Topic topic);

    /**
     * 转换TopicList为VOList
     * @author ONeofSu
     * @param topics List<Topic>
     * @return List<TopicVo>
     */
    List<TopicVo> transferTopicToVO(List<Topic> topics);

    /**
     * 转换DTO为Topic
     * @author ONeofSu
     * @param topicDTO TopicDTO
     * @return Topic
     */
    Topic transferDTOToTopic(TopicDTO topicDTO);

    /**
     * 检查id是否存在
     * @author ONeofSu
     * @param topicId Long
     * @return boolean
     */
    boolean isTopicExist(Long topicId);

    /**
     * 检查topicName是否存在
     * @author ONeofSu
     * @param topicName String
     * @return boolean
     */
    boolean isTopicExist(String topicName);
}
