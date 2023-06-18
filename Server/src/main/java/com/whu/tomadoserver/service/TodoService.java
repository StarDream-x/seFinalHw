package com.whu.tomadoserver.service;

import com.whu.tomadoserver.dao.TodoJPARepository;
import com.whu.tomadoserver.entity.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hiroxzwang
 * @create 2023/6/17 14:43
 */
@Service
public class TodoService {
    @Autowired
    TodoJPARepository todoRepository;

    public TodoItem addTodo(TodoItem todo) {
        return todoRepository.save(todo);
    }

    public TodoItem getTodo(long id) {
        return todoRepository.getById(id);
    }
    public List<TodoItem> getTodoByUserId(long userId) {
        return todoRepository.getByUserId(userId);
    }

    public void updateTodo(long id, TodoItem todo) {
        todoRepository.save(todo);
    }

    public void deleteTodo(long id) {
        todoRepository.deleteById(id);
    }

    public List<TodoItem> findTodos(String name, Boolean complete) {
        //动态构造查询条件，name和complete不为null时作为条件
        Specification<TodoItem> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (name != null) {
                predicateList.add(criteriaBuilder.like(root.get("taskName"), "%" + name + "%"));
            }
            if ((complete != null)) {
                predicateList.add(criteriaBuilder.equal(root.get("isDone"), complete));
            }
            Predicate[] predicates = predicateList.toArray(new Predicate[predicateList.size()]);
            return criteriaBuilder.and(predicates);
        };

        List<TodoItem> result = todoRepository.findAll(specification);
        return result;
    }

}