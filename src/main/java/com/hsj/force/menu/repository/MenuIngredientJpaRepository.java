package com.hsj.force.menu.repository;

import com.hsj.force.domain.dto.MenuIngredientListDTO;
import com.hsj.force.domain.entity.TMenuIngredient;
import com.hsj.force.domain.entity.embedded.TMenuIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuIngredientJpaRepository extends JpaRepository<TMenuIngredient, TMenuIngredientId> {

    @Query("select new com.hsj.force.domain.dto.MenuIngredientListDTO(" +
            "mi.menuNo, " +
            "i.quantity, " +
            "mi.ingredientNo, " +
            "mi.quantity) " +
            "from TMenuIngredient mi " +
            "join TIngredient i on mi.ingredientNo = i.ingredientNo ")
    List<MenuIngredientListDTO> findMenuIngredientListDTOV1();

    @Query("select new com.hsj.force.domain.dto.MenuIngredientListDTO(" +
            "mi.menuNo, " +
            "i.quantity, " +
            "mi.ingredientNo, " +
            "mi.quantity) " +
            "from TMenuIngredient mi " +
            "join TIngredient i on mi.ingredientNo = i.ingredientNo " +
            "and mi.menuNo = :menuNo")
    List<MenuIngredientListDTO> findMenuIngredientListDTOV2(@Param("menuNo") String menuNo);

    @Query("select new com.hsj.force.domain.dto.MenuIngredientListDTO(" +
            "mi.menuNo, " +
            "mi.ingredientNo, " +
            "i.quantity, " +
            "mi.quantity)  " +
            "from TMenuIngredient mi " +
            "join TIngredient i on mi.ingredientNo = i.ingredientNo ")
    List<MenuIngredientListDTO> findMenuIngredientListDTOV3();

    @Query("select new com.hsj.force.domain.dto.MenuIngredientListDTO(" +
            "o.menu.menuNo, " +
            "mi.ingredientNo, " +
            "o.quantity, " +
            "i.quantity, " +
            "mi.quantity)  " +
            "from TOrder o " +
            "join TMenuIngredient mi on o.menu.menuNo = mi.menuNo " +
            "join TIngredient i on mi.ingredientNo = i.ingredientNo " +
            "where o.orderNo = :orderNo " +
            "and o.menu.menuNo = :menuNo " +
            "and o.orderStatus.orderStatusNo = :orderStatusNo")
    List<MenuIngredientListDTO> findMenuIngredientListDTOV4(@Param("orderNo") String orderNo, @Param("menuNo") String menuNo, @Param("orderStatusNo") String orderStatusNo);

    List<TMenuIngredient> findAllByMenuNo(String menuNo);

    @Modifying(clearAutomatically = true)
    int deleteByMenuNo(@Param("menuNo") String menuNo);
}
