package com.csu.research.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.research.entity.Team;

import java.util.List;

public interface TeamService {

    // 按照名字搜索团队，数据分页
    Page<Team> searchTeamByName(Page<Team> page, String query);

    // 获得所有的团队
    List<Team> findAllTeams();

    // 根据团队Id获取换取团队信息
    Team findTeamByTeamId(Long teamId);

    // 更新团队信息
    boolean updateTeam(Team team);

    // 删除团队信息
    boolean deleteTeam(Long teamId);

    // 新增团队
    boolean addTeam(Team team);
}
