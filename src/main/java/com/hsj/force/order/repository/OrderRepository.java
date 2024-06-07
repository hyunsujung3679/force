package com.hsj.force.order.repository;

import com.hsj.force.domain.entity.TOrder;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public List<TOrder> findAllV2(String storeNo) {
        return em.createQuery(
                "select o, m " +
                        "from TOrder o, TMenu m " +
                        "where o.menu.menuNo = m.menuNo " +
                        "and o.orderStatus.orderStatusNo = :orderStatusNo " +
                        "and m.saleStatus.saleStatusNo = :saleStatusNo " +
                        "and o.tOrderId.store.storeNo = :storeNo", TOrder.class)
                .setParameter("orderStatusNo", "OS001")
                .setParameter("saleStatusNo", "SS001")
                .setParameter("storeNo", storeNo)
                .getResultList();
    }
}
