package com.whu.tomadoserver.service;

import com.whu.tomadoserver.dao.NodoJPARepository;
import com.whu.tomadoserver.entity.NodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 孔德昱
 * @date 2023/6/18 20:23 星期日
 */
@Service
public class NodoService {
    @Autowired
    NodoJPARepository nodoRepository;

    public NodoItem addNodo(NodoItem nodo) {
        return nodoRepository.save(nodo);
    }

    public NodoItem getNodo(long id) {
        return nodoRepository.getById(id);
    }
    public List<NodoItem> getNodoByUserId(long userId) {
        return nodoRepository.getByUserId(userId);
    }

    public void updateNodo(long id, NodoItem nodo) {
        nodoRepository.save(nodo);
    }

    public void deleteNodo(long id) {
        nodoRepository.deleteById(id);
    }

    public List<NodoItem> findNodos(String name, Boolean complete) {
        //动态构造查询条件，name和complete不为null时作为条件
        Specification<NodoItem> specification = (root, query, criteriaBuilder) -> {
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

        List<NodoItem> result = nodoRepository.findAll(specification);
        return result;
    }
}
