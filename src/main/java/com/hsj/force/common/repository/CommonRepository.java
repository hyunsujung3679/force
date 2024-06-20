package com.hsj.force.common.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommonRepository {

    private final EntityManager em;

    public Optional<String> findStoreName(String storeNo) {
        List<String> storeName = em.createQuery(
                "select s.storeName " +
                        "from TStore s " +
                        "where s.storeNo = :storeNo", String.class)
                .setParameter("storeNo", storeNo)
                .getResultList();
        return storeName.stream().findAny();
    }
}
