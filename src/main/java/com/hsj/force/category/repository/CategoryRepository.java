package com.hsj.force.category.repository;

import com.hsj.force.domain.entity.TCategory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final EntityManager em;


    public List<TCategory> findCategoryByOrderForm(String storeNo) {
        return em.createQuery(
                "select c " +
                        "from TCategory c " +
                        "where c.useYn = :useYn " +
                        "and c.storeNo = :storeNo " +
                        "order by c.priority asc", TCategory.class)
                .setParameter("useYn", 1)
                .setParameter("storeNo", storeNo)
                .getResultList();
    }
}
