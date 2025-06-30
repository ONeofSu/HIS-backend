package com.csu.research.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csu.research.entity.Team;
import com.csu.research.mapper.TeamMapper;
import com.csu.research.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    @Autowired
    private TeamMapper teamMapper;

    @Override
    public Page<Team> searchTeamByName(Page<Team> page, String query) {
        return baseMapper.searchTeamByName(page, query);
    }

    @Override
    public List<Team> findAllTeams() {
        return teamMapper.selectList(null);
    }

    @Override
    public Team findTeamByTeamId(Long teamId) {
        return teamMapper.selectById(teamId);
    }

    @Override
    public boolean updateTeam(Team team) {
        return teamMapper.updateById(team) == 1;
    }

    @Override
    public boolean deleteTeam(Long teamId) {
        return teamMapper.deleteById(teamId) == 1;
    }

    @Override
    public boolean addTeam(Team team) {
        return teamMapper.insert(team) == 1;
    }
}
