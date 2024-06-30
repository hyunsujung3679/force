package com.hsj.force.order.repository;

import com.hsj.force.domain.dto.OrderListDTO;
import com.hsj.force.domain.dto.QOrderListDTO;
import com.hsj.force.domain.entity.TOrder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.hsj.force.domain.entity.QTMenu.tMenu;
import static com.hsj.force.domain.entity.QTOrder.tOrder;
import static org.springframework.util.StringUtils.hasText;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<OrderListDTO> findOrderListDTO(String orderStatusNo, String saleStatusNo) {
        return queryFactory
                .select(new QOrderListDTO(
                        tOrder.orderNo,
                        tOrder.orderSeq,
                        tMenu.menuNo,
                        tMenu.menuName,
                        tOrder.salePrice,
                        tOrder.quantity,
                        tOrder.discountPrice,
                        tOrder.totalSalePrice,
                        tOrder.fullPriceYn,
                        tOrder.fullPerYn,
                        tOrder.selPriceYn,
                        tOrder.selPerYn,
                        tOrder.serviceYn,
                        tOrder.table.tableNo,
                        tOrder.orderStatus.orderStatusNo))
                .from(tOrder)
                .join(tOrder.menu, tMenu)
                .where(tOrder.orderStatus.orderStatusNo.eq(orderStatusNo)
                        .and(tMenu.saleStatus.saleStatusNo.eq(saleStatusNo)))
                .fetch();
    }

    @Override
    public TOrder findOneCustom(String orderNo, String tableNo, String menuNo, String orderStatusNo) {
        return queryFactory
                .selectFrom(tOrder)
                .where(
                        orderNoEq(orderNo),
                        tableNoEq(tableNo),
                        menuNoEq(menuNo),
                        orderStatusNoEq(orderStatusNo)
                ).fetchOne();

    }

    @Override
    public List<TOrder> findAllCustomV1(String orderNo, String tableNo, String orderStatusNo) {
        return queryFactory
                .selectFrom(tOrder)
                .where(
                        orderNoEq(orderNo),
                        tableNoEq(tableNo),
                        orderStatusNoEq(orderStatusNo)
                ).fetch();
    }

    private BooleanExpression orderNoEq(String orderNo) {
        return hasText(orderNo) ? tOrder.orderNo.eq(orderNo) : null;
    }

    private BooleanExpression tableNoEq(String tableNo) {
        return hasText(tableNo) ? tOrder.table.tableNo.eq(tableNo) : null;
    }

    private BooleanExpression menuNoEq(String menuNo) {
        return hasText(menuNo) ? tOrder.menu.menuNo.eq(menuNo) : null;
    }

    private BooleanExpression orderStatusNoEq(String orderStatusNo) {
        return hasText(orderStatusNo) ? tOrder.orderStatus.orderStatusNo.eq(orderStatusNo) : null;
    }
}
