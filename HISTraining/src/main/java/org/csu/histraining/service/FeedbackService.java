package org.csu.histraining.service;

import org.csu.histraining.DTO.FeedbackDTO;
import org.csu.histraining.VO.FeedbackVO;
import org.csu.histraining.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    public int addFeedback(Feedback feedback);
    public Feedback getFeedbackById(int feedbackId);
    public List<Feedback> getAllFeedback();
    public List<Feedback> getFeedbackByUserId(int userId);
    public boolean isFeedbackIdExist(int feedbackId);
    public boolean deleteFeedback(int feedbackId);

    public Feedback transferFeedbackDTOToFeedback(FeedbackDTO feedbackDTO,int userId);
    public FeedbackVO transferToFeedbackVO(Feedback feedback);
    public List<FeedbackVO> transferToFeedbackVOList(List<Feedback> feedbackList);
}
