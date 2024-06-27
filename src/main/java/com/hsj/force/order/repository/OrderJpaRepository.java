package com.hsj.force.order.repository;

import com.hsj.force.domain.dto.OrderListDTO;
import com.hsj.force.domain.dto.TableListDTO;
import com.hsj.force.domain.entity.TMenu;
import com.hsj.force.domain.entity.TOrder;
import com.hsj.force.domain.entity.TOrderStatus;
import com.hsj.force.domain.entity.TTable;
import com.hsj.force.domain.entity.embedded.TOrderId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<TOrder, TOrderId> {

    @Query("select o " +
            "from TOrder o " +
            "where o.table.tableNo = :tableNo " +
            "and o.menu.menuNo = :menuNo " +
            "and o.orderStatus.orderStatusNo = :orderStatusNo")
    TOrder findOneV1(@Param("tableNo") String tableNo, @Param("menuNo") String menuNo, @Param("orderStatusNo") String orderStatusNo);

    @Query("select o " +
            "from TOrder o " +
            "where o.orderNo = :orderNo " +
            "and o.table.tableNo = :tableNo " +
            "and o.menu.menuNo = :menuNo " +
            "and o.orderStatus.orderStatusNo = :orderStatusNo")
    TOrder findOneV2(@Param("orderNo") String orderNo, @Param("tableNo") String tableNo, @Param("menuNo") String menuNo, @Param("orderStatusNo") String orderStatusNo);

    @Query("select o " +
            "from TOrder o " +
            "where o.orderNo = :orderNo " +
            "and o.table.tableNo = :tableNo " +
            "and o.orderStatus.orderStatusNo = :orderStatusNo")
    List<TOrder> findAllV1(@Param("orderNo") String orderNo, @Param("tableNo") String tableNo, @Param("orderStatusNo") String orderStatusNo);

    @Query("select distinct new com.hsj.force.domain.dto.TableListDTO(t.tableNo, t.tableName) " +
            "from TOrder o " +
            "join TTable t on o.table.tableNo = t.tableNo " +
            "where o.orderStatus.orderStatusNo = :orderStatusNo " +
            "order by t.tableNo asc")
    List<TableListDTO> findAllV2(@Param("orderStatusNo") String orderStatusNo);

    @EntityGraph(attributePaths = {"menu"})
    List<TOrder> findAllByOrderStatusAndTable(TOrderStatus orderStatus, TTable table);

    @Query("select new com.hsj.force.domain.dto.OrderListDTO("+
            " o.orderNo," +
            " o.orderSeq," +
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
            " o.table.tableNo," +
            " o.orderStatus.orderStatusNo) " +
            "from TOrder o " +
            "join o.menu m " +
            "where o.orderStatus.orderStatusNo = :orderStatusNo " +
            "and m.saleStatus.saleStatusNo = :saleStatusNo ")
    List<OrderListDTO> findOrderListDTO(@Param("orderStatusNo") String orderStatusNo, @Param("saleStatusNo") String saleStatusNo);

    @EntityGraph(attributePaths = {"table"})
    TOrder findFirstByTableOrderByOrderNoDesc(TTable table);

    TOrder findFirstByOrderByOrderNoDesc();

    List<TOrder> findAllByOrderNo(@Param("orderNo") String orderNo);

    TOrder findFirstByOrderNoOrderByOrderSeqDesc(@Param("orderNo") String orderNo);

    TOrder findOneByOrderNoAndMenu(@Param("orderNo") String orderNo, TMenu menu);

    List<TOrder> findAllByOrderNoAndOrderStatus(@Param("orderNo") String orderNo, TOrderStatus orderStatus);

}
