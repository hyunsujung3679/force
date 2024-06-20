package com.hsj.force.ingredient.repository;

import com.hsj.force.domain.dto.IngredientListDTO;
import com.hsj.force.domain.dto.MenuIngredientListDTO;
import com.hsj.force.domain.entity.TInDeReason;
import com.hsj.force.domain.entity.TIngredient;
import com.hsj.force.domain.entity.TIngredientHis;
import com.hsj.force.domain.entity.embedded.TIngredientId;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IngredientRepository {

    private final EntityManager em;

    public TIngredient findIngredient(TIngredientId ingredientId) {
        return em.find(TIngredient.class, ingredientId);
    }

    public Optional<String> findIngredientSeq(String ingredientNo, String storeNo) {
        List<String> ingredientSeq = em.createQuery(
                        "select ih.ingredientHisId.ingredientSeq " +
                                "from TIngredientHis ih " +
                                "where ih.ingredientHisId.ingredientNo = :ingredientNo " +
                                "and ih.ingredientHisId.storeNo = :storeNo " +
                                "order by ih.ingredientHisId.ingredientSeq desc " +
                                "limit 1", String.class)
                .setParameter("ingredientNo", ingredientNo)
                .setParameter("storeNo", storeNo)
                .getResultList();
        return ingredientSeq.stream().findAny();
    }

    public void saveIngredient(TIngredient ingredient) {
        em.persist(ingredient);
    }

    public TInDeReason findInDeReason(String inDeReasonNo) {
        return em.find(TInDeReason.class, inDeReasonNo);
    }

    public void saveIngredientHis(TIngredientHis ingredientHis) {
        em.persist(ingredientHis);
    }

    public List<MenuIngredientListDTO> findMenuIngredientList(String storeNo, String orderNo, String menuNo) {
        return em.createQuery(
                "select new com.hsj.force.domain.dto.MenuIngredientListDTO(o.menu.menuNo, mi.menuIngredientId.ingredientNo, o.quantity, i.quantity, mi.quantity)  " +
                        "from TOrder o " +
                        "join TMenuIngredient mi on o.menu.menuNo = mi.menuIngredientId.menuNo " +
                        "join TIngredient i on mi.menuIngredientId.ingredientNo = i.ingredientId.ingredientNo " +
                        "where o.orderId.orderNo = :orderNo " +
                        "and o.table.storeNo = :storeNo " +
                        "and o.menu.menuNo = :menuNo " +
                        "and o.orderStatus.orderStatusNo = :orderStatusNo", MenuIngredientListDTO.class)
                .setParameter("orderNo", orderNo)
                .setParameter("storeNo", storeNo)
                .setParameter("menuNo", menuNo)
                .setParameter("orderStatusNo", "OS001")
                .getResultList();
    }

    public List<MenuIngredientListDTO> findMenuIngredientListV2(String storeNo) {
        return em.createQuery(
                        "select new com.hsj.force.domain.dto.MenuIngredientListDTO(mi.menuIngredientId.menuNo, mi.menuIngredientId.ingredientNo, i.quantity, mi.quantity)  " +
                                "from TMenuIngredient mi " +
                                "join TIngredient i on mi.menuIngredientId.ingredientNo = i.ingredientId.ingredientNo " +
                                "where i.ingredientId.storeNo = :storeNo ", MenuIngredientListDTO.class)
                .setParameter("storeNo", storeNo)
                .getResultList();
    }

    public List<TIngredient> findIngredientV2(String storeNo) {
        return em.createQuery(
                "select i " +
                        "from TIngredient i " +
                        "where i.ingredientId.storeNo = :storeNo " +
                        "order by i.ingredientId.ingredientNo asc", TIngredient.class)
                .setParameter("storeNo", storeNo)
                .getResultList();
    }

    public Optional<String> findIngredientNo(String storeNo) {
        List<String> ingredientNo = em.createQuery(
                        "select i.ingredientId.ingredientNo " +
                                "from TIngredient i " +
                                "where i.ingredientId.storeNo = : storeNo " +
                                "order by i.ingredientId.ingredientNo desc " +
                                "limit 1", String.class)
                .setParameter("storeNo", storeNo)
                .getResultList();
        return ingredientNo.stream().findAny();
    }

    public List<TInDeReason> findInDeReasonV1() {
        return em.createQuery(
                "select idr " +
                        "from TInDeReason idr", TInDeReason.class)
                .getResultList();
    }

    public double findIngredientQuantity(String ingredientNo, String storeNo) {
        return em.createQuery(
                "select i.quantity " +
                        "from TIngredient i " +
                        "where i.ingredientId.storeNo = :storeNo " +
                        "and i.ingredientId.ingredientNo = :ingredientNo", Double.class)
                .setParameter("storeNo", storeNo)
                .setParameter("ingredientNo", ingredientNo)
                .getSingleResult();
    }
}
