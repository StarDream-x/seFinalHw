package com.whu.tomadoserver.dao;

import com.whu.tomadoserver.entity.TeamItem;
import com.whu.tomadoserver.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author hiroxzwang
 * @create 2023/6/17 16:25
 */
public interface TeamJPARepository extends JpaRepository<TeamItem, Long>, JpaSpecificationExecutor<TodoItem> {
    @Query(value = "select * from team_item where team_name = ?1 and team_password = ?2", nativeQuery = true)
    List<TeamItem> findByTeamNameAndTeamPassword(String teamName, String teamPassword);
}
