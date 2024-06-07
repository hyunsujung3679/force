package com.hsj.force.table.repository;

import com.hsj.force.domain.entity.TTable;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TableRepository {

    private final EntityManager em;

    public List<TTable> findAll(String storeNo) {
        return em.createQuery(
                "select t " +
                        "from TTable t " +
                        "where t.store.storeNo = :storeNo " +
                        "order by t.tableNo asc", TTable.class)
                .setParameter("storeNo", storeNo)
                .getResultList();
    }
}
