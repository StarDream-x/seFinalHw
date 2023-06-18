package com.whu.tomadoserver.service;

import com.whu.tomadoserver.dao.ProfileDao;
import com.whu.tomadoserver.entity.ProfileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 孔德昱
 * @date 2023/6/17 11:22 星期六
 */
@Service
public class ProfileService {
    @Autowired
    ProfileDao profileDao;

    public String login(String username, String password) {
//         通过username和password查询数据库，看是否存在
        List<ProfileItem> profiles = profileDao.findByUsernameAndPassword(username, password);
        if (profiles == null || profiles.size() == 0) {
            return "False";
        } else {
            return profiles.get(0).getUserId()+" True";
        }
    }

    // 注册
    public String register(String username, String password) {
//         通过username查询数据库，看是否存在
        List<ProfileItem> profiles = profileDao.findByUsername(username);
        if (profiles == null || profiles.size() == 0) {
            // 不存在，可以注册
            ProfileItem profileItem = new ProfileItem();
            profileItem.setUsername(username);
            profileItem.setPassword(password);
            profileDao.save(profileItem);
            return "True";
        } else {
            return "用户名已存在";
        }
    }
}
