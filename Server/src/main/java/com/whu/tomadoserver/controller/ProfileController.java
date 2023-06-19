package com.whu.tomadoserver.controller;

import com.whu.tomadoserver.entity.ProfileItem;
import com.whu.tomadoserver.service.ProfileService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 孔德昱
 * @date 2023/6/17 11:14 星期六
 */
@Api(description = "用户管理")
@RestController
public class ProfileController {
    @Autowired
    ProfileService profileService;

    // 参数包括：账号，密码
    @GetMapping("/login")
    public String login(@Param("username") String username, @Param("password") String password) {
        return profileService.login(username, password);
    }

    @GetMapping("/register")
    public String register(@Param("username") String username, @Param("password") String password) {
        return profileService.register(username, password);
    }

    @GetMapping("/manageTeam")
    public ProfileItem getManageTeam(@Param("userId") Long uid) {
        if(uid==null) {return null;}
        return profileService.getManageTeam(uid);
    }

    // id:userid, addTeamId:teamid
    @PutMapping("/{id}/{addTeamId}")
    public ResponseEntity<Void>updateProfile(@PathVariable long id,@PathVariable long addTeamId, @RequestBody ProfileItem profile){
        profileService.updateProfileManageTeams(addTeamId, profile);
        return ResponseEntity.ok().build();
    }

}
