package com.hsj.force.table.repository;

import com.hsj.force.domain.entity.TOrder;
import com.hsj.force.domain.entity.TTable;
import com.hsj.force.domain.entity.embedded.TTableId;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TableRepository {

    private final EntityManager em;

    public List<TTable> findAll(String storeNo) {
        return em.createQuery(
                "select t " +
                        "from TTable t " +
                        "where t.storeNo = :storeNo " +
                        "order by t.tableNo asc", TTable.class)
                .setParameter("storeNo", storeNo)
                .getResultList();
    }

    public TTable findTable(TTableId tableId) {
        return em.find(TTable.class, tableId);
    }

}
