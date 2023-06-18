package com.whu.tomadoserver.dao;

import com.whu.tomadoserver.entity.TeamItem;
import com.whu.tomadoserver.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author hiroxzwang
 * @create 2023/6/17 16:25
 */
public interface TeamJPARepository extends JpaRepository<TeamItem, Long>, JpaSpecificationExecutor<TodoItem> {
}
