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

    public List<TMenuIngredient> findMenuIngredient(String storeNo) {
        String sql = "SELECT MI.MENU_NO, " +
                            "MI.INGREDIENT_NO, " +
                            "MI.QUANTITY AS NEED_QUANTITY, " +
                            "MI.QUANTITY AS STOCK_QUANTITY " +
                       "FROM TMENUINGREDIENT MI, TINGREIDNET I " +
                      "WHERE MI.INGREDIENT_NO = I.INGREDIENT_NO " +
                        "AND MI.STORE_NO = I.STORE_NO " +
                        "AND MI.STORE_NO = ?";

        return em.createNativeQuery(sql, TMenuIngredient.class)
                .setParameter(1, storeNo)
                .getResultList();
    }
}
