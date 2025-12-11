package org.csu.histraining.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.csu.histraining.DTO.FeedbackDTO;
import org.csu.histraining.VO.FeedbackVO;
import org.csu.histraining.entity.Feedback;
import org.csu.histraining.mapper.FeedbackMapper;
import org.csu.histraining.service.FeedbackService;
import org.csu.histraining.service.MaterialService;
import org.csu.histraining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    FeedbackMapper feedbackMapper;
    @Autowired
    MaterialService materialService;
    @Autowired
    UserService userService;

    private boolean isInputFeedbackValid(Feedback feedback) {
        if(!materialService.isMaterialIdExist(feedback.getMaterialId())) {
            return false;
        }
        if(!userService.isUserIdExist(feedback.getUserId())) {
            return false;
        }
        if(feedback.getContent() == null || feedback.getContent().equals("")) {
            return false;
        }
        return true;
    }

    @Override
    public int addFeedback(Feedback feedback) {
        if(!isInputFeedbackValid(feedback)) {
            return -1;
        }
        feedbackMapper.insert(feedback);
        return feedback.getId();
    }

    @Override
    public Feedback getFeedbackById(int feedbackId) {
        return feedbackMapper.selectById(feedbackId);
    }

    @Override
    public List<Feedback> getAllFeedback() {
        return feedbackMapper.selectList(null);
    }

    @Override
    public List<Feedback> getFeedbackByUserId(int userId) {
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return feedbackMapper.selectList(queryWrapper);
    }

    @Override
    public boolean isFeedbackIdExist(int feedbackId) {
        return feedbackMapper.selectById(feedbackId) != null;
    }

    @Override
    public boolean deleteFeedback(int feedbackId) {
        if(!isFeedbackIdExist(feedbackId)) {
            return false;
        }
        feedbackMapper.deleteById(feedbackId);
        return true;
    }

    @Override
    public Feedback transferFeedbackDTOToFeedback(FeedbackDTO feedbackDTO,int userId) {
        Feedback feedback = new Feedback();
        feedback.setMaterialId(feedbackDTO.getMaterialId());
        feedback.setUserId(userId);
        feedback.setContent(feedbackDTO.getContent());
        feedback.setTime(new Timestamp(System.currentTimeMillis()));
        feedback.setRating(feedbackDTO.getRating());
        return feedback;
    }

    @Override
    public FeedbackVO transferToFeedbackVO(Feedback feedback) {
        FeedbackVO feedbackVO = new FeedbackVO();
        feedbackVO.setId(feedback.getId());
        feedbackVO.setMaterialId(feedback.getMaterialId());
        feedbackVO.setRating(feedback.getRating());
        feedbackVO.setContent(feedback.getContent());
        feedbackVO.setUserId(feedback.getUserId());
        feedbackVO.setTime(feedback.getTime());
        feedbackVO.setUserName(userService.getUsernameById(feedback.getUserId()));
        return feedbackVO;
    }

    @Override
    public List<FeedbackVO> transferToFeedbackVOList(List<Feedback> feedbackList) {
        List<FeedbackVO> feedbackVOList = new ArrayList<>();
        for(Feedback feedback : feedbackList) {
            feedbackVOList.add(transferToFeedbackVO(feedback));
        }
        return feedbackVOList;
    }
}
