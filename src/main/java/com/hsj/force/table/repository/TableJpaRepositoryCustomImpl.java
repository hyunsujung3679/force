package com.hsj.force.table.repository;

import com.hsj.force.domain.dto.QTableListDTO;
import com.hsj.force.domain.dto.TableListDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.hsj.force.domain.entity.QTOrder.tOrder;
import static com.hsj.force.domain.entity.QTTable.tTable;
import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static org.springframework.util.StringUtils.hasText;

public class TableJpaRepositoryCustomImpl implements TableJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TableJpaRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<TableListDTO> findTableListDTOV1(String orderStatusNo) {
        return queryFactory
                .select(new QTableListDTO(
                        tTable.tableNo,
                        tTable.tableName
                )).distinct()
                .from(tTable)
                .join(tOrder).on(tOrder.table.tableNo.eq(tTable.tableNo))
                .where(
                        orderStatusNoEq(orderStatusNo)
                )
                .orderBy(tTable.tableNo.asc())
                .fetch();
    }

    @Override
    public List<TableListDTO> findTableListDTOV2(String orderStatusNo) {
        return queryFactory
                .select(new QTableListDTO(
                        tTable.tableNo,
                        tTable.tableName
                ))
                .from(tTable)
                .where(
                        selectFrom(tOrder)
                                .where(tTable.tableNo.eq(tOrder.table.tableNo)
                                        .and(tOrder.orderStatus.orderStatusNo.eq(orderStatusNo))).notExists()
                )
                .orderBy(tTable.tableNo.asc())
                .fetch();
    }

    private BooleanExpression orderStatusNoEq(String orderStatusNo) {
        return hasText(orderStatusNo) ? tOrder.orderStatus.orderStatusNo.eq(orderStatusNo) : null;
    }
}
