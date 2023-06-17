package com.whu.tomadoserver.dao;

import com.whu.tomadoserver.entity.ProfileItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 孔德昱
 * @date 2023/6/17 11:34 星期六
 * @description 数据库操作
 */
public interface ProfileDao extends JpaRepository<ProfileItem, Long>, JpaSpecificationExecutor<ProfileItem> {
    @Query(value = "select * from profile_item where username = ?1 and password = ?2", nativeQuery = true)
    List<ProfileItem> findByUsernameAndPassword(String username, String password);

    @Query(value = "select * from profile_item where username = ?1", nativeQuery = true)
    List<ProfileItem> findByUsername(String username);
}
