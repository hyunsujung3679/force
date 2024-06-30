package com.hsj.force.open.repository;

import com.hsj.force.domain.entity.TOpenClose;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

import static com.hsj.force.domain.entity.QTOpenClose.tOpenClose;
import static com.querydsl.core.types.dsl.Expressions.stringTemplate;

public class OpenRepositoryCustomImpl implements OpenRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OpenRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public TOpenClose findOneV1() {
        return queryFactory
                .selectFrom(tOpenClose)
                .where(tOpenClose.closeMoney.isNotNull()
                        .and(stringTemplate(
                                "DATE_FORMAT({0}, {1})"
                                , tOpenClose.insertDate
                                , ConstantImpl.create("%Y-%m-%d")).eq(String.valueOf(LocalDate.now()))))
                .orderBy(tOpenClose.openCloseSeq.desc())
                .fetchOne();
    }
}
