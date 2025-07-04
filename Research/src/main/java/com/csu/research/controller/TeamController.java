package com.csu.research.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.research.entity.Team;
import com.csu.research.entity.TeamMember;
import com.csu.research.mapper.TeamMapper;
import com.csu.research.service.AuthService;
import com.csu.research.service.TeamService;
import com.csu.research.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/teams")
public class TeamController {
    @Autowired
    TeamService teamService;
    @Autowired
    AuthService authService;
    @Autowired
    UserService userService;

    @GetMapping("/search")
    public ResponseEntity<?> searchTeam(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String query
    ) {
        log.info("search team by name: {}, page: {}, size: {}",
                query, current, size);
        Page<Team> page = new Page<>(current, size);
        Page<Team> resultPage = teamService.searchTeamByName(page, query);

        return ResponseEntity.ok(
                Map.of("code", 0,
                        "teams", resultPage.getRecords(),
                        "total", resultPage.getTotal())
        );
    }

    @GetMapping("/")
    public ResponseEntity<?> findAll() {
        List<Team> teams = teamService.findAllTeams();
        return ResponseEntity.ok(
                Map.of("code", 0,
                "teams", teams));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<?> findById(
            @PathVariable Long teamId
    ) {
        Team team = teamService.findTeamByTeamId(teamId);

        if (team == null) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","the team doesn't exist")
            );
        }

        return ResponseEntity.ok(
                Map.of("code", 0,
                        "team", team)
        );

    }

    @PutMapping("/")
    public ResponseEntity<?> update(
            @RequestBody Team team) {
        Team oriTeam = teamService.findTeamByTeamId(team.getTeamId());
        if (oriTeam == null) {
            return ResponseEntity.ok(
                    Map.of("code", -1,
                            "message", "invalid teamId")
            );
        }

        boolean success = teamService.updateTeam(team);
        if (success) {
            return ResponseEntity.ok(
                    Map.of("code", 0,
                            "success", true)
            );

        }
        return ResponseEntity.ok(
                Map.of("code", -2,
                        "message", "update team failed")
        );

    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> delete(
            @PathVariable Long teamId) {
        Team team = teamService.findTeamByTeamId(teamId);
        if (team == null) {
            return ResponseEntity.ok(
                    Map.of("code", -1,
                            "message", "invalid teamId")
            );
        }

        boolean success = teamService.deleteTeam(teamId);
        if (success) {
            return ResponseEntity.ok(
                    Map.of("code", 0,
                            "success", true)
            );
        }

        return ResponseEntity.ok(
                Map.of("code", -2,
                        "message", "delete team failed")
        );
    }

    @PostMapping("/add")
    public ResponseEntity<?> add (
            @RequestBody Team team) {
         team = teamService.addTeam(team);
        if (team != null) {
            return ResponseEntity.ok(
                    Map.of("code", 0,
                            "team", team)
            );
        }
        return ResponseEntity.ok(
                Map.of("code", -1,
                        "message", "add team failed")
        );
    }

    @PostMapping("/member")
    public ResponseEntity<?> addMember(@RequestBody TeamMember teamMember) {
        if(!teamService.isExistTeam(teamMember.getTeamId())) {
            return ResponseEntity.ok(
                    Map.of("code", -1,
                            "message", "team doesn't exist")
            );
        }
        if(teamMember.getUserId() != null && !userService.isUserIdExist(teamMember.getUserId())){
            return ResponseEntity.ok(
                    Map.of("code", -2,
                            "message", "user doesn't exist")
            );
        }

        //队伍中第一个成员默认为队长
        boolean flag = false;
        List<TeamMember> teamMates = teamService.getTeamMemberByTeamId(teamMember.getTeamId());
        if(teamMates == null || teamMates.size() == 0) {
            flag = true;
        }

        teamMember = teamService.addTeamMember(teamMember);
        if(teamMember == null) {
            return ResponseEntity.ok(
                    Map.of("code", -3,
                            "message", "add team team member failed")
            );
        }
        if(flag){
            teamService.setTeamCaptain(teamMember);
        }

        return ResponseEntity.ok(
                Map.of("code",0,
                        "teamMamber", teamMember)
        );
    }

    @DeleteMapping("member/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long memberId) {
        if(!teamService.isExistTeamMember(memberId)) {
            return ResponseEntity.ok(
                    Map.of("code", -1,
                            "message", "team member doesn't exist")
            );
        }
        if(!teamService.deleteTeamMember(memberId)) {
            return ResponseEntity.ok(
                    Map.of("code", -2,
                            "message", "delete team team member failed")
            );
        }
        return ResponseEntity.ok(
                Map.of("code",0,
                        "memberId", memberId)
        );
    }

    @GetMapping("/{teamId}/member/all")
    public ResponseEntity<?> findAllMemberInTeam(@PathVariable Long teamId) {
        Team team = teamService.findTeamByTeamId(teamId);
        if (team == null) {
            return ResponseEntity.ok(
                    Map.of("code", -1,
                            "message", "team doesn't exist")
            );
        }

        List<TeamMember> teamMembers = teamService.getTeamMemberByTeamId(teamId);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "teamMembers", teamMembers)
        );
    }

    @PutMapping("/member/{memberId}")
    public ResponseEntity<?> updateMember(@PathVariable Long memberId,
                                          @RequestBody TeamMember teamMember) {
        if(!teamService.isExistTeamMember(memberId)) {
            return ResponseEntity.ok(
                    Map.of("code", -1,
                            "message", "team member doesn't exist")
            );
        }
        if(!teamService.isExistTeam(teamMember.getTeamId())) {
            return ResponseEntity.ok(
                    Map.of("code", -2,
                            "message", "team doesn't exist")
            );
        }
        teamMember.setTeamMemberId(memberId);
        teamMember = teamService.updateTeamMember(teamMember);
        if(teamMember == null) {
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message", "update team member failed")
            );
        }

        return ResponseEntity.ok(
                Map.of("code",0,
                        "teamMember", teamMember)
        );
    }

    @PostMapping("/captain/{memberId}")
    public ResponseEntity<?> addCaptain(@RequestHeader("Authorization") String authHeader,
                                        @PathVariable Long memberId) {
        if(!teamService.isExistTeamMember(memberId)) {
            return ResponseEntity.ok(
                    Map.of("code", -1,
                            "message", "team member doesn't exist")
            );
        }

        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(userId == -1) {
            return ResponseEntity.ok(
                    Map.of("code", -2,
                            "message", "invalid token")
            );
        }

        // TODO: 可以让管理员也具有权限

        //检测用户是否有权限设置队长
        if(!authService.isQualifiedToSetCaptain(userId, memberId)) {
            return ResponseEntity.ok(
                    Map.of("code", -3,
                            "message", "you're not qualified to set captain")
            );
        }

        TeamMember teamMember = teamService.getTeamMemberById(memberId);
        teamMember = teamService.setTeamCaptain(teamMember);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "teamMember", teamMember)
        );
    }
}
