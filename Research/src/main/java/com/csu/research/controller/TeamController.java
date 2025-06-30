package com.csu.research.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.research.entity.Team;
import com.csu.research.service.TeamService;
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

    // todo: 似乎没有真正分页
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
    public Map<String, Object> findAll() {
        List<Team> teams = teamService.findAllTeams();
        return Map.of("code", 0,
                "teams", teams);
    }

    @GetMapping("/{teamId}")
    public Map<String, Object> findById(
            @PathVariable Long teamId
    ) {
        Team team = teamService.findTeamByTeamId(teamId);
        return Map.of("code", 0,
                "team", team);
    }

    @PutMapping("/")
    public Map<String, Object> update(
            @RequestBody Team team
    ) {
        boolean success = teamService.updateTeam(team);
        if (success) {
            return Map.of("code", 0,
                    "success", true);
        }
        return Map.of("code", -1,
                "message", "update team failed");
    }

    @DeleteMapping("/{teamId}")
    public Map<String, Object> delete(
            @PathVariable Long teamId
    ) {
        boolean success = teamService.deleteTeam(teamId);
        if (success) {
            return Map.of("code", 0,
                    "success", true);
        }
        return Map.of("code", -1,
                "message", "delete team failed");
    }

    @PostMapping("/add")
    public Map<String, Object> add (
            @RequestBody Team team
    ) {
        boolean success = teamService.addTeam(team);
        if (success) {
            return Map.of("code", 0,
                    "success", true);
        }
        return Map.of("code", -1,
                "message", "add team failed");
    }

}
