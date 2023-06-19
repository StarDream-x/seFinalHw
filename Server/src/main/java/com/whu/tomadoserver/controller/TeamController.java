package com.whu.tomadoserver.controller;

import com.whu.tomadoserver.entity.ProfileItem;
import com.whu.tomadoserver.entity.TeamItem;
import com.whu.tomadoserver.entity.TodoItem;
import com.whu.tomadoserver.service.ProfileService;
import com.whu.tomadoserver.service.TeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hiroxzwang
 * @create 2023/6/17 16:24
 */
@Api(description = "待办事项管理")
@RestController
@RequestMapping("teams")
public class TeamController {
    @Autowired
    TeamService teamService;

    @Autowired
    ProfileService profileService;

    @ApiOperation("查询所有团队信息")
    @GetMapping("")
    public ResponseEntity<List<TeamItem>> findTeams(){
        List<TeamItem> result = teamService.findTeams();
        return ResponseEntity.ok(result);
    }

    @ApiOperation("查询团队Todo")
    @GetMapping("/todos/{id}")
    public ResponseEntity<List<TodoItem>> findTeamTodos(@ApiParam("团队Id")@PathVariable long id){
        List<TodoItem> result = teamService.findTodos(id);
        return ResponseEntity.ok(result);
    }
    @ApiOperation("添加团队")
    @PostMapping("")
    public ResponseEntity<TeamItem> addTeam(@RequestBody TeamItem team){
        try {
            TeamItem result = teamService.addTeam(team);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(team);
        }
    }

    @ApiOperation("加入团队")
    @PostMapping("/join")
    public ResponseEntity<TeamItem> joinTeam(@RequestBody TeamItem team){
        long userId = Long.parseLong(team.getIdList());
        TeamItem result = teamService.findTeamByNameAndPassword(team.getTeamName(),team.getTeamPassword());
        if(result==null) {
            return ResponseEntity.noContent().build();
        }
        if(teamService.findMems(result.getId()).contains(userId)){
            return ResponseEntity.badRequest().body(result);
        }
        teamService.addTeamMem(result, userId);
        ProfileItem profile = profileService.getProfileById(userId);
        profileService.updateProfileJoinTeams(result.getId(),profile);
        return ResponseEntity.ok(result);
    }
}
