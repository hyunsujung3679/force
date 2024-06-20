package com.hsj.force.order.repository;

import com.hsj.force.domain.dto.OrderListDTO;
import com.hsj.force.domain.entity.TInDeReason;
import com.hsj.force.domain.entity.TOrder;
import com.hsj.force.domain.entity.TOrderStatus;
import com.hsj.force.domain.entity.embedded.TOrderId;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public List<TOrder> findAll(String storeNo, String tableNo) {
        return em.createQuery(
                "select o " +
                        "from TOrder o " +
                        "join fetch o.menu m " +
                        "where o.orderStatus.orderStatusNo = :orderStatusNo " +
                        "and o.table.storeNo = :storeNo " +
                        "and o.table.tableNo = :tableNo", TOrder.class)
                .setParameter("orderStatusNo", "OS001")
                .setParameter("storeNo", storeNo)
                .setParameter("tableNo", tableNo)
                .getResultList();
    }

    public List<OrderListDTO> findAllV2(String storeNo) {
        return em.createQuery(
                "select new com.hsj.force.domain.dto.OrderListDTO("+
                        " o.orderId.orderNo," +
                        " o.orderId.orderSeq," +
                        " m.menuNo," +
                        " m.menuName," +
                        " o.salePrice," +
                        " o.quantity," +
                        " o.discountPrice," +
                        " o.totalSalePrice," +
                        " o.fullPriceYn," +
                        " o.fullPerYn," +
                        " o.selPriceYn," +
                        " o.selPerYn," +
                        " o.serviceYn," +
                        " o.table.storeNo," +
                        " o.table.tableNo," +
                        " o.orderStatus.orderStatusNo) " +
                        "from TOrder o " +
                        "join o.menu m " +
                        "where o.orderStatus.orderStatusNo = :orderStatusNo " +
                        "and m.saleStatus.saleStatusNo = :saleStatusNo " +
                        "and o.table.storeNo = :storeNo", OrderListDTO.class)
                .setParameter("orderStatusNo", "OS001")
                .setParameter("saleStatusNo", "SS001")
                .setParameter("storeNo", storeNo)
                .getResultList();
    }

    public Optional<String> findOrderNo(String storeNo, String tableNo) {
        List<String> orderNo = em.createQuery(
                        "select o.orderId.orderNo " +
                                "from TOrder o " +
                                "join TTable t on o.table.tableNo = t.tableNo " +
                                "where o.table.storeNo = :storeNo " +
                                "and t.tableNo = : tableNo " +
                                "order by o.orderId.orderNo desc limit 1", String.class)
                .setParameter("storeNo", storeNo)
                .setParameter("tableNo", tableNo)
                .getResultList();
        return orderNo.stream().findAny();
    }

    public Optional<String> findLastOrderNo(String storeNo) {
        List<String> orderNo = em.createQuery(
                        "select o.orderId.orderNo " +
                                "from TOrder o " +
                                "where o.table.storeNo = :storeNo " +
                                "order by o.orderId.orderNo desc limit 1", String.class)
                .setParameter("storeNo", storeNo)
                .getResultList();
        return orderNo.stream().findAny();
    }

    public List<String> findOrderStatusNoList(String orderNo) {
        return em.createQuery(
                "select o.orderStatus.orderStatusNo " +
                        "from TOrder o " +
                        "where o.orderId.orderNo = : orderNo", String.class)
                .setParameter("orderNo", orderNo)
                .getResultList();
    }

    public Optional<Integer> findOrderQuantity(String storeNo, String tableNo, String menuNo) {
        List<Integer> orderQuantity = em.createQuery(
                        "select o.quantity " +
                                "from TOrder o " +
                                "where o.table.storeNo = : storeNo " +
                                "and o.table.tableNo = :tableNo " +
                                "and o.menu.menuNo = : menuNo " +
                                "and o.orderStatus.orderStatusNo = : orderStatusNo", Integer.class)
                .setParameter("storeNo", storeNo)
                .setParameter("tableNo", tableNo)
                .setParameter("menuNo", menuNo)
                .setParameter("orderStatusNo", "OS001")
                .getResultList();
        return orderQuantity.stream().findAny();
    }

    public Optional<String> findOrderSeq(String newOrderNo) {
        List<String> orderSeq = em.createQuery(
                        "select o.orderId.orderSeq " +
                                "from TOrder o " +
                                "where o.orderId.orderNo = :orderNo " +
                                "order by o.orderId.orderSeq desc " +
                                "limit 1", String.class)
                .setParameter("orderNo", newOrderNo)
                .getResultList();
        return orderSeq.stream().findAny();
    }

    public TOrderStatus findOrderStatus(String orderStatusNo) {
        return em.find(TOrderStatus.class, orderStatusNo);
    }

    public void saveOrder(TOrder order) {
        em.persist(order);
    }

    public TOrder findOrder(TOrderId orderId) {
        return em.find(TOrder.class, orderId);
    }

    public List<TOrder> findOrderList(String storeNo, String tableNo) {
        return em.createQuery(
                "select o " +
                        "from TOrder o " +
                        "join fetch o.menu m " +
                        "where o.table.storeNo = :storeNo " +
                        "and o.table.tableNo = :tableNo " +
                        "and o.orderStatus.orderStatusNo = :orderStatusNo", TOrder.class)
                .setParameter("storeNo", storeNo)
                .setParameter("tableNo", tableNo)
                .setParameter("orderStatusNo", "OS001")
                .getResultList();
    }

    public Optional<TOrder> findOrderByOrderNoAndMenuNo(String orderNo, String menuNo) {
        List<TOrder> order = em.createQuery(
                        "select o " +
                                "from TOrder o " +
                                "where o.orderId.orderNo = :orderNo " +
                                "and o.menu.menuNo = :menuNo", TOrder.class)
                .setParameter("orderNo", orderNo)
                .setParameter("menuNo", menuNo)
                .getResultList();
        return order.stream().findAny();
    }

    public List<String> findMenuNoList(String storeNo, String orderNo) {
        return em.createQuery(
                "select o.menu.menuNo " +
                        "from TOrder o " +
                        "where o.table.storeNo = :storeNo " +
                        "and o.orderId.orderNo = :orderNo " +
                        "and o.orderStatus.orderStatusNo = :orderStatusNo", String.class)
                .setParameter("storeNo", storeNo)
                .setParameter("orderNo", orderNo)
                .setParameter("orderStatusNo", "OS001")
                .getResultList();

    }

    public List<TOrder> findOrderV1(String storeNo, String orderNo, String tableNo) {
        return em.createQuery(
                        "select o " +
                                "from TOrder o " +
                                "where o.table.storeNo = :storeNo " +
                                "and o.orderId.orderNo = :orderNo " +
                                "and o.table.tableNo = :tableNo " +
                                "and o.orderStatus.orderStatusNo = :orderStatusNo", TOrder.class)
                .setParameter("storeNo", storeNo)
                .setParameter("orderNo", orderNo)
                .setParameter("tableNo", tableNo)
                .setParameter("orderStatusNo", "OS001")
                .getResultList();
    }

    public Optional<TOrder> findOrderV2(String storeNo, String orderNo, String tableNo, String menuNo) {
        List<TOrder> order = em.createQuery(
                        "select o " +
                                "from TOrder o " +
                                "where o.table.storeNo = :storeNo " +
                                "and o.orderId.orderNo = :orderNo " +
                                "and o.table.tableNo = :tableNo " +
                                "and o.menu.menuNo = :menuNo " +
                                "and o.orderStatus.orderStatusNo = :orderStatusNo", TOrder.class)
                .setParameter("storeNo", storeNo)
                .setParameter("orderNo", orderNo)
                .setParameter("tableNo", tableNo)
                .setParameter("menuNo", menuNo)
                .setParameter("orderStatusNo", "OS001")
                .getResultList();
        return order.stream().findAny();
    }

    public List<TOrder> findOrderV3(String storeNo, String tableNo) {
        return em.createQuery(
                        "select o " +
                                "from TOrder o " +
                                "where o.table.storeNo = :storeNo " +
                                "and o.table.tableNo = :tableNo " +
                                "and o.orderStatus.orderStatusNo = :orderStatusNo", TOrder.class)
                .setParameter("storeNo", storeNo)
                .setParameter("tableNo", tableNo)
                .setParameter("orderStatusNo", "OS001")
                .getResultList();
    }
}
