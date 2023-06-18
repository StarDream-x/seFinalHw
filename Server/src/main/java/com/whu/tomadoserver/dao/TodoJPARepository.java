package com.whu.tomadoserver.dao;

import com.whu.tomadoserver.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author hiroxzwang
 * @create 2023/6/17 14:40
 */
public interface TodoJPARepository extends JpaRepository<TodoItem, Long>, JpaSpecificationExecutor<TodoItem> {
    //根据userId查找待办事项
    @Query(value = "select * from todo_item where user_id = ?1", nativeQuery = true)
    List<TodoItem> getByUserId(long userId);

}
