package com.whu.tomadoserver.service;

import com.whu.tomadoserver.dao.ProfileJPARepository;
import com.whu.tomadoserver.dao.TodoJPARepository;
import com.whu.tomadoserver.entity.ProfileItem;
import com.whu.tomadoserver.entity.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 孔德昱
 * @date 2023/6/17 11:22 星期六
 */
@Service
public class ProfileService {
    @Autowired
    ProfileJPARepository profileJPARepository;

    public String login(String username, String password) {
//         通过username和password查询数据库，看是否存在
        List<ProfileItem> profiles = profileJPARepository.findByUsernameAndPassword(username, password);
        if (profiles == null || profiles.size() == 0) {
            return "False";
        } else {
            return profiles.get(0).getUserId()+" True";
        }
    }

    // 注册
    public String register(String username, String password) {
//         通过username查询数据库，看是否存在
        List<ProfileItem> profiles = profileJPARepository.findByUsername(username);
        if (profiles == null || profiles.size() == 0) {
            // 不存在，可以注册
            ProfileItem profileItem = new ProfileItem();
            profileItem.setUsername(username);
            profileItem.setPassword(password);
            profileJPARepository.save(profileItem);
            return "True";
        } else {
            return "用户名已存在";
        }
    }

    public ProfileItem getManageTeam(Long uid) {
        Specification<ProfileItem> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (uid != null) {
                predicateList.add(criteriaBuilder.equal(root.get("userId"), uid));
            }
            Predicate[] predicates = predicateList.toArray(new Predicate[predicateList.size()]);
            return criteriaBuilder.and(predicates);
        };

        List<ProfileItem> reslis = profileJPARepository.findAll(specification);
        if(reslis.size()==0) {return null;}
        ProfileItem result = reslis.get(0);
        result.setPassword("******");
        return result;
    }
    public void updateProfile(long id, ProfileItem profile) {
        ProfileItem user = profileJPARepository.getById(profile.getUserId());
        user.setManageTeams(user.getManageTeams()+","+id);
        profileJPARepository.save(user);
    }
}
