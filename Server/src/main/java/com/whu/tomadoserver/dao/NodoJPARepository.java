package com.whu.tomadoserver.dao;

import com.whu.tomadoserver.entity.NodoItem;
import com.whu.tomadoserver.entity.NodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 孔德昱
 * @date 2023/6/18 20:22 星期日
 */
public interface NodoJPARepository extends JpaRepository<NodoItem, Long>, JpaSpecificationExecutor<NodoItem> {
    //根据userId查找待办事项
    @Query(value = "select * from nodo_item where user_id = ?1", nativeQuery = true)
    List<NodoItem> getByUserId(long userId);
}
