package com.csu.research.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csu.research.entity.Team;
import com.csu.research.entity.TeamMember;
import com.csu.research.mapper.TeamMapper;
import com.csu.research.mapper.TeamMemberMapper;
import com.csu.research.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    @Autowired
    private TeamMapper teamMapper;
    @Autowired
    private TeamMemberMapper teamMemberMapper;

    @Override
    public Page<Team> searchTeamByName(Page<Team> page, String query) {
        return baseMapper.searchTeamByName(page, query);
    }

    @Override
    public List<Team> findAllTeams() {
        QueryWrapper<Team> queryWrapper = new QueryWrapper();
        queryWrapper.eq("team_isvalid",true);
        return teamMapper.selectList(queryWrapper);
    }

    @Override
    public Team findTeamByTeamId(Long teamId) {
        Team team = teamMapper.selectById(teamId);
        if (team == null || !team.isTeamIsvalid()) {
            return null;
        }
        return team;
    }

    @Override
    public boolean updateTeam(Team team) {
        Team ori = teamMapper.selectById(team.getTeamId());
        if (ori == null || !ori.isTeamIsvalid()) {
            return false;
        }
        teamMapper.updateById(team);
        return true;
    }

    @Override
    public boolean deleteTeam(Long teamId) {
        Team team = teamMapper.selectById(teamId);
        if (team == null || !team.isTeamIsvalid()) {
            return false;
        }
        team.setTeamIsvalid(false);
        teamMapper.updateById(team);
        return true;
    }

    @Override
    public Team addTeam(Team team) {
        team.setTeamIsvalid(true);
        teamMapper.insert(team);
        return team;
    }

    @Override
    public boolean isExistTeam(Long teamId) {
        Team team = teamMapper.selectById(teamId);
        return team != null && team.isTeamIsvalid();
    }

    @Override
    public TeamMember addTeamMember(TeamMember teamMember) {
        teamMemberMapper.insert(teamMember);
        return teamMember;
    }

    @Override
    public boolean deleteTeamMember(Long teamMemberId) {
        return teamMemberMapper.deleteById(teamMemberId) > 0;
    }

    @Override
    public List<TeamMember> getTeamMemberByTeamId(Long teamId) {
        QueryWrapper<TeamMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamId);
        return teamMemberMapper.selectList(queryWrapper);
    }

    @Override
    public TeamMember updateTeamMember(TeamMember teamMember) {
        teamMemberMapper.updateById(teamMember);
        return teamMemberMapper.selectById(teamMember.getTeamMemberId());
    }

    @Override
    public TeamMember setTeamCaptain(TeamMember teamMember) {
        QueryWrapper<TeamMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("team_id", teamMember.getTeamId());
        List<TeamMember> members = teamMemberMapper.selectList(queryWrapper);
        for (TeamMember member : members) {
            member.setTeamMemberIsCaptain(false);
            teamMemberMapper.updateById(member);
        }
        teamMember.setTeamMemberIsCaptain(true);
        teamMemberMapper.updateById(teamMember);
        return teamMember;
    }

    @Override
    public boolean isExistTeamMember(Long teamMemberId) {
        TeamMember teamMember = teamMemberMapper.selectById(teamMemberId);
        return teamMember!= null;
    }

    @Override
    public boolean isUserInTeam(int userId, Long teamId) {
        List<TeamMember> teamMembers = getTeamMemberByTeamId(teamId);
        for (TeamMember teamMember : teamMembers) {
            if (teamMember.getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public TeamMember getTeamMemberById(Long teamMemberId) {
        return teamMemberMapper.selectById(teamMemberId);
    }
}
