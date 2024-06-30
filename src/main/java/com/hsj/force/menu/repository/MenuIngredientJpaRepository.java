package com.hsj.force.menu.repository;

import com.hsj.force.domain.entity.TMenuIngredient;
import com.hsj.force.domain.entity.embedded.TMenuIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuIngredientJpaRepository extends JpaRepository<TMenuIngredient, TMenuIngredientId>, MenuIngredientJpaRepositoryCustom {

    List<TMenuIngredient> findAllByMenuNo(String menuNo);

    @Modifying(clearAutomatically = true)
    int deleteByMenuNo(@Param("menuNo") String menuNo);
}
