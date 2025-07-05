package com.csu.research.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csu.research.entity.*;
import com.csu.research.mapper.AuthMapper;
import com.csu.research.mapper.ContentMapper;
import com.csu.research.mapper.TeamMapper;
import com.csu.research.mapper.TeamMemberMapper;
import com.csu.research.service.AuthService;
import com.csu.research.service.ContentService;
import com.csu.research.service.TeamService;
import com.csu.research.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    AuthMapper authMapper;
    @Autowired
    TeamService teamService;
    @Autowired
    TopicService topicService;
    @Autowired
    TeamMemberMapper teamMemberMapper;
    @Autowired
    ContentMapper contentMapper;

    @Override
    public Auth getAuthById(Long id) {
        return authMapper.selectById(id);
    }

    @Override
    public Auth getAuthByName(String name) {
        QueryWrapper<Auth> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("auth_name", name);
        return authMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean isQualifiedToSetCaptain(int userId, Long teamMemberId) {
        TeamMember teamMember = teamService.getTeamMemberById(teamMemberId);

        Long teamId = teamMember.getTeamId();
        QueryWrapper<TeamMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId).eq("user_id", userId);
        TeamMember user = teamMemberMapper.selectOne(queryWrapper);
        if (user == null) {
            return false;
        }
        return user.isTeamMemberIsCaptain();
    }

    @Override
    public boolean isQualifiedToAddContent(Content content, int userId) {
        Topic topic = topicService.getTopicById(content.getTopicId());
        Team team = teamService.findTeamByTeamId(topic.getTeamId());
        List<TeamMember> teamMembers = teamService.getTeamMemberByTeamId(team.getTeamId());
        //System.out.println("isQualifiedToAddContent" + topic.toString() +team.toString() + teamMembers.toString());

        for(TeamMember teamMember : teamMembers) {
            //System.out.println(teamMember);
            if(teamMember.getUserId()!=null && teamMember.getUserId() == userId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isQualifiedToReadContent(Content content,int userId) {
        Auth auth = authMapper.selectById(content.getAuthId());
        if(auth == null){
            return true;    //未设置视为公开
        }
        if(auth.getAuthId()==1){
            //私有 尽自己可见
            return userId == content.getUserId();
        }else if(auth.getAuthId()==2) {
            //仅团队成员可见
            Topic topic = topicService.getTopicById(content.getTopicId());
            return teamService.isUserInTeam(userId,topic.getTeamId());
        }else{
            return true;
        }
    }

    @Override
    public boolean isQualifiedToReadContent(Long contentId, int userId) {
        QueryWrapper<Content> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content_id", contentId).eq("content_isvalid", 1);
        Content content = contentMapper.selectOne(queryWrapper);
        //System.out.println(content);
        if(content == null){
            return false;
        }
        //System.out.println(isQualifiedToReadContent(content,userId));
        return isQualifiedToReadContent(content,userId);
    }

    @Override
    public boolean isQualifiedToWriteContent(Content content, int userId) {
        //判断是否为创建者
        if(content.getUserId() == userId){
            return true;
        }

        //判断是否为队伍队长
        Topic topic = topicService.getTopicById(content.getTopicId());
        Team team = teamService.findTeamByTeamId(topic.getTeamId());
        List<TeamMember> teamMembers = teamService.getTeamMemberByTeamId(team.getTeamId());
        int captainId = 0;

        for(TeamMember teamMember : teamMembers) {
            if(teamMember.isTeamMemberIsCaptain()){
                captainId = teamMember.getUserId();
            }
        }
        if(captainId == userId){
            return true;
        }

        return false;
    }

    @Override
    public boolean isQualifiedToWriteContent(Long contentId, int userId) {
        QueryWrapper<Content> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content_id", contentId).eq("content_isvalid", true);
        Content content = contentMapper.selectOne(queryWrapper);
        if(content == null){
            return false;
        }
        return isQualifiedToWriteContent(content, userId);
    }
}
