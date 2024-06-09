package com.hsj.force.menu.repository;

import com.hsj.force.domain.entity.TIngredient;
import com.hsj.force.domain.entity.TMenuIngredient;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MenuRepository {

    private final EntityManager em;

//    public List<TMenuIngredient> findMenuIngredient(String storeNo) {
//        String sql = "SELECT MI.MENU_N"
//
//        return em.createQuery("select mi " +
//                        "from TMenuIngredient mi " +
//                        "join TIngredient i " +
//                        "on mi.menuIngredientId.ingredientNo = i.ingredientId.ingredientNo " +
//                        "and ", TMenuIngredient.class)
////                .setParameter(1, storeNo)
//                .getResultList();
//    }
}
