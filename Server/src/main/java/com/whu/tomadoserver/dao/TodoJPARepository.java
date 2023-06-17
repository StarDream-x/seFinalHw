package com.whu.tomadoserver.dao;

import com.whu.tomadoserver.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author hiroxzwang
 * @create 2023/6/17 14:40
 */
public interface TodoJPARepository extends JpaRepository<TodoItem, Long>, JpaSpecificationExecutor<TodoItem> {
}
