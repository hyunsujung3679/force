package com.hsj.force.close.repository;

import com.hsj.force.domain.entity.TOpenClose;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CloseRepository {

    private final EntityManager em;

    public Optional<TOpenClose> findOpenCloseV1(String storeNo) {
        List<TOpenClose> openclose = em.createQuery(
                        "select oc " +
                                "from TOpenClose oc " +
                                "where oc.openCloseId.storeNo = :storeNo " +
                                "and oc.closeMoney is null", TOpenClose.class)
                .setParameter("storeNo", storeNo)
                .getResultList();
        return openclose.stream().findAny();
    }
}
