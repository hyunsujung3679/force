package com.hsj.force.category.repository;

import com.hsj.force.domain.entity.TCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<TCategory, String> {

    List<TCategory> findAllByUseYnOrderByPriority(@Param("useYn") String useYn);

    List<TCategory> findAllByOrderByPriority();

    TCategory findFirstByOrderByCategoryNoDesc();

    TCategory findOneByPriority(@Param("priority") int priority);

    TCategory findFirstByOrderByPriorityDesc();

    TCategory findFirstByUseYnOrderByPriority(@Param("useYn") String useYn);
}
