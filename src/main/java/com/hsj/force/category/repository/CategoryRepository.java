package com.hsj.force.category.repository;

import com.hsj.force.domain.entity.TCategory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public Optional<String> findFirstCategoryNo(String storeNo) {
        List<String> categoryNo = em.createQuery(
                        "select c.categoryNo " +
                                "from TCategory c " +
                                "where c.storeNo = :storeNo " +
                                "and useYn = :useYn " +
                                "order by c.priority asc " +
                                "limit 1", String.class)
                .setParameter("storeNo", storeNo)
                .setParameter("useYn", "1")
                .getResultList();
        return categoryNo.stream().findAny();
    }

    public List<TCategory> findCategoryV1(String storeNo) {
        return em.createQuery(
                "select c " +
                        "from TCategory c " +
                        "where c.storeNo = :storeNo " +
                        "order by c.priority asc", TCategory.class)
                .setParameter("storeNo", storeNo)
                .getResultList();
    }

    public Optional<String> findMaxCategoryNo(String storeNo) {
        List<String> categoryNo = em.createQuery(
                "select max(c.categoryNo) " +
                        "from TCategory c " +
                        "where c.storeNo = :storeNo", String.class)
                .setParameter("storeNo", storeNo)
                .getResultList();
        return categoryNo.stream().findAny();
    }

    public void saveCategory(TCategory category) {
        em.persist(category);
    }

    public Optional<TCategory> findCategoryV2(String storeNo, int priority) {
        List<TCategory> category = em.createQuery(
                        "select c " +
                                "from TCategory c " +
                                "where c.storeNo = :storeNo " +
                                "and c.priority = :priority", TCategory.class)
                .setParameter("storeNo", storeNo)
                .setParameter("priority", priority)
                .getResultList();
        return category.stream().findAny();
    }

    public int findMaxPriority(String storeNo) {
        return em.createQuery(
                        "select max(c.priority) " +
                                "from TCategory c " +
                                "where c.storeNo = :storeNo", Integer.class)
                .setParameter("storeNo", storeNo)
                .getSingleResult();

    }

    public Optional<TCategory> findCategoryV3(String storeNo, String categoryNo) {
        List<TCategory> category = em.createQuery("select c from TCategory c where c.storeNo = : storeNo and c.categoryNo = :categoryNo", TCategory.class)
                .setParameter("storeNo", storeNo)
                .setParameter("categoryNo", categoryNo)
                .getResultList();
        return category.stream().findAny();
    }

    public TCategory findCategory(String categoryNo) {
        return em.find(TCategory.class, categoryNo);
    }
}
