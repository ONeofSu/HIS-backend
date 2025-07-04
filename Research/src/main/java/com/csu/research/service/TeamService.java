package com.csu.research.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.research.entity.Team;
import com.csu.research.entity.TeamMember;

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
    Team addTeam(Team team);

    //团队是否存在
    boolean isExistTeam(Long teamId);

    //新增团队成员
    TeamMember addTeamMember(TeamMember teamMember);

    //删除团队成员
    boolean deleteTeamMember(Long teamMemberId);

    //查看团队下的所有团队成员信息
    List<TeamMember> getTeamMemberByTeamId(Long teamId);

    //修改团队成员信息
    TeamMember updateTeamMember(TeamMember teamMember);

    //将某人设为团队队长
    TeamMember setTeamCaptain(TeamMember teamMember);

    //团队成员是否存在
    boolean isExistTeamMember(Long teamMemberId);

    //用户是否在队伍中
    boolean isUserInTeam(int userId, Long teamId);

    //获得TeamMember
    TeamMember getTeamMemberById(Long teamMemberId);
}
