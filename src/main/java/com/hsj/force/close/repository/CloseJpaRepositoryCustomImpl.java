package com.hsj.force.close.repository;

import com.hsj.force.domain.dto.OpenCloseUpdateDTO;
import com.hsj.force.domain.dto.QOpenCloseUpdateDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.util.StringUtils;

import static com.hsj.force.domain.entity.QTOpenClose.tOpenClose;
import static com.hsj.force.domain.entity.QTOrder.tOrder;
import static com.querydsl.jpa.JPAExpressions.select;

public class CloseJpaRepositoryCustomImpl implements CloseJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CloseJpaRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public OpenCloseUpdateDTO findSumInfo() {
        return queryFactory
                .select(new QOpenCloseUpdateDTO(
                        tOrder.totalSalePrice.sum().as("sumSalePrice"),
                        tOrder.count().as("sumOrderCnt"),
                        Expressions.constant(0),
                        Expressions.constant(0L),
                        Expressions.constant(0),
                        Expressions.constant(0),
                        Expressions.constant(0L)))
                .from(tOrder)
                .where(
                        orderDateGoeModifyDate()
                )
                .fetchOne();
    }

    @Override
    public OpenCloseUpdateDTO findCancelInfo(String orderStatusNo) {
        return queryFactory
                .select(new QOpenCloseUpdateDTO(
                        Expressions.constant(0),
                        Expressions.constant(0L),
                        tOrder.totalSalePrice.sum().as("sumCancelPrice"),
                        tOrder.count().as("sumCancelCnt"),
                        Expressions.constant(0),
                        Expressions.constant(0),
                        Expressions.constant(0L)))
                .from(tOrder)
                .where(
                        orderDateGoeModifyDate(),
                        orderStatusNoEq(orderStatusNo)
                )
                .fetchOne();
    }

    @Override
    public OpenCloseUpdateDTO findDiscountPrice(String orderStatusNo) {
        return queryFactory
                .select(new QOpenCloseUpdateDTO(
                        Expressions.constant(0),
                        Expressions.constant(0L),
                        Expressions.constant(0),
                        Expressions.constant(0L),
                        tOrder.discountPrice.sum().as("discountPrice"),
                        Expressions.constant(0),
                        Expressions.constant(0L)))
                .from(tOrder)
                .where(
                        orderDateGoeModifyDate(),
                        orderStatusNoEq(orderStatusNo)
                )
                .fetchOne();
    }

    @Override
    public OpenCloseUpdateDTO findRealOrderInfo(String orderStatusNo) {
        return queryFactory
                .select(new QOpenCloseUpdateDTO(
                        Expressions.constant(0),
                        Expressions.constant(0L),
                        Expressions.constant(0),
                        Expressions.constant(0L),
                        Expressions.constant(0),
                        tOrder.totalSalePrice.sum().as("totalSalePrice"),
                        tOrder.count().as("realOrderCnt")))
                .from(tOrder)
                .where(
                        orderDateGoeModifyDate(),
                        orderStatusNoEq(orderStatusNo)
                )
                .fetchOne();
    }

    private BooleanExpression orderDateGoeModifyDate() {
        return tOrder.orderDate.goe(
                select(tOpenClose.modifyDate)
                        .from(tOpenClose)
                        .where(tOpenClose.closeMoney.isNull()));
    }

    private BooleanExpression orderStatusNoEq(String orderStatusNo) {
        return StringUtils.hasText(orderStatusNo)? tOrder.orderStatus.orderStatusNo.eq(orderStatusNo) : null;
    }

}
