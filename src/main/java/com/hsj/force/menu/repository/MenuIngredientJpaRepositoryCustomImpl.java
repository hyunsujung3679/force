package com.hsj.force.menu.repository;

import com.hsj.force.domain.dto.MenuIngredientListDTO;
import com.hsj.force.domain.dto.QMenuIngredientListDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.hsj.force.domain.entity.QTIngredient.tIngredient;
import static com.hsj.force.domain.entity.QTMenuIngredient.tMenuIngredient;
import static com.hsj.force.domain.entity.QTOrder.tOrder;
import static com.querydsl.core.types.dsl.Expressions.constant;
import static org.springframework.util.StringUtils.hasText;

public class MenuIngredientJpaRepositoryCustomImpl implements MenuIngredientJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MenuIngredientJpaRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MenuIngredientListDTO> findMenuIngredientListDTOV1(String menuNo) {
        return queryFactory
                .select(new QMenuIngredientListDTO(
                        tMenuIngredient.menuNo,
                        tIngredient.ingredientNo,
                        tMenuIngredient.quantity,
                        tIngredient.quantity,
                        constant(0),
                        tIngredient.quantity,
                        tMenuIngredient.quantity))
                .from(tMenuIngredient)
                .join(tIngredient).on(tMenuIngredient.ingredientNo.eq(tIngredient.ingredientNo))
                .where(menuNoEq(menuNo))
                .fetch();
    }

    @Override
    public List<MenuIngredientListDTO> findMenuIngredientListDTOV2(String menuNo, String orderNo, String orderStatusNo) {
        return queryFactory
                .select(new QMenuIngredientListDTO(
                        tMenuIngredient.menuNo,
                        tIngredient.ingredientNo,
                        tMenuIngredient.quantity,
                        tIngredient.quantity,
                        tOrder.quantity,
                        tIngredient.quantity,
                        tMenuIngredient.quantity))
                .from(tMenuIngredient)
                .join(tIngredient).on(tMenuIngredient.ingredientNo.eq(tIngredient.ingredientNo))
                .join(tOrder).on(tMenuIngredient.menuNo.eq(tOrder.menu.menuNo))
                .where(
                        menuNoEq(menuNo),
                        orderNoEq(orderNo),
                        orderStatusNoEq(orderStatusNo)
                )
                .fetch();
    }

    private BooleanExpression menuNoEq(String menuNo) {
        return hasText(menuNo) ? tMenuIngredient.menuNo.eq(menuNo) : null;
    }

    private BooleanExpression orderNoEq(String orderNo) {
        return hasText(orderNo) ? tOrder.orderNo.eq(orderNo) : null;
    }

    private BooleanExpression orderStatusNoEq(String orderStatusNo) {
        return hasText(orderStatusNo) ? tOrder.orderStatus.orderStatusNo.eq(orderStatusNo) : null;
    }
}
