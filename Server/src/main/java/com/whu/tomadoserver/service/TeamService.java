package com.whu.tomadoserver.service;

import com.whu.tomadoserver.dao.TeamJPARepository;
import com.whu.tomadoserver.dao.TodoJPARepository;
import com.whu.tomadoserver.entity.TeamItem;
import com.whu.tomadoserver.entity.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hiroxzwang
 * @create 2023/6/17 16:25
 */
@Service
public class TeamService {

    @Autowired
    TeamJPARepository teamRepository;
    @Autowired
    TodoJPARepository todoRepository;

    public List<TodoItem> findTodos(long id) {
        List<Long> memIds = findMems(id);
        List<TodoItem> result = new ArrayList<TodoItem>();
        for(Long uid: memIds){
            List<TodoItem> todos = findTodosByUid(uid);
            for(TodoItem todo: todos){
                result.add(todo);
            }
        }
        return result;
    }

    public List<Long> findMems(long id) {
        String memList;
        try{
            TeamItem sres = teamRepository.getById(id);
            memList = sres.getIdList();
        }catch (Exception e){
            memList = "";
        }
        List<Long> result = new ArrayList<Long>();
        long x=0;char ch;
        for(int i = 0; i < memList.length(); ++i) {
            ch = memList.charAt(i);
            if (ch >= 48 && ch <= 57) {
                x = (x << 3) + (x << 1) + ch - 48;
            } else {
                result.add(x);
                x = 0;
            }
        }
        result.add(x);
        if(!result.contains(0L)) {result.add(0L);}
        return result;
    }

    public List<TodoItem> findTodosByUid(Long uid) {
        Specification<TodoItem> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (uid != null) {
                predicateList.add(criteriaBuilder.equal(root.get("userId"), uid));
            }
            Predicate[] predicates = predicateList.toArray(new Predicate[predicateList.size()]);
            return criteriaBuilder.and(predicates);
        };

        List<TodoItem> result = todoRepository.findAll(specification);
        return result;
    }

    public List<TeamItem> findTeams(){
        return teamRepository.findAll();
    }
    public TeamItem addTeam(TeamItem team) {
        return teamRepository.save(team);
    }
}
