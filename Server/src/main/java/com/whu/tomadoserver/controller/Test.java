package com.whu.tomadoserver.controller;

import com.whu.tomadoserver.entity.TeamItem;
import com.whu.tomadoserver.entity.TodoItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 孔德昱
 * @date 2023/6/18 13:24 星期日
 */
@Api(description = "待办事项管理")
@RestController
public class Test {
    @ApiOperation("查询团队Todo")
    @GetMapping("/test")
    public ResponseEntity<TeamItem> findTeamTodos(){
        TeamItem teamItem = new TeamItem(1, "team1","1,2");
        return ResponseEntity.ok(teamItem);
    }
}
