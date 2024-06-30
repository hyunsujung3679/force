package com.hsj.force.menu.repository;

import com.hsj.force.domain.dto.MenuPriceDTO;
import com.hsj.force.domain.dto.QMenuPriceDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import static com.hsj.force.domain.entity.QTMenu.tMenu;
import static com.hsj.force.domain.entity.QTMenuPrice.tMenuPrice;

public class MenuPriceJpaRepositoryCustomImpl implements MenuPriceJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MenuPriceJpaRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public MenuPriceDTO findMenuPriceDTO(String menuNo) {
        return queryFactory
                .select(new QMenuPriceDTO(
                        tMenuPrice.menuNo,
                        tMenuPrice.menuSeq,
                        tMenu.menuName,
                        tMenu.saleStatus.saleStatusNo,
                        tMenu.category.categoryNo,
                        tMenuPrice.salePrice,
                        tMenu.imageSaveName
                        ))
                .from(tMenuPrice)
                .join(tMenu).on(tMenuPrice.menuNo.eq(tMenu.menuNo))
                .orderBy(tMenuPrice.menuSeq.desc())
                .limit(1)
                .fetchOne();
    }
}
