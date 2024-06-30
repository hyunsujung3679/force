package com.hsj.force.order.repository;

import com.hsj.force.domain.entity.TOrder;
import com.hsj.force.domain.entity.TOrderStatus;
import com.hsj.force.domain.entity.TTable;
import com.hsj.force.domain.entity.embedded.TOrderId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<TOrder, TOrderId>, OrderRepositoryCustom {

    @EntityGraph(attributePaths = {"menu"})
    List<TOrder> findAllByOrderStatusAndTable(TOrderStatus orderStatus, TTable table);

    @EntityGraph(attributePaths = {"table"})
    TOrder findFirstByTableOrderByOrderNoDesc(TTable table);

    TOrder findFirstByOrderByOrderNoDesc();

    List<TOrder> findAllByOrderNo(@Param("orderNo") String orderNo);

    TOrder findFirstByOrderNoOrderByOrderSeqDesc(@Param("orderNo") String orderNo);

    List<TOrder> findAllByOrderNoAndOrderStatus(@Param("orderNo") String orderNo, TOrderStatus orderStatus);

}
