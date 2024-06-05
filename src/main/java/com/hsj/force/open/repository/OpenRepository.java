package com.hsj.force.open.repository;

import com.hsj.force.domain.entity.TOpenClose;
import com.hsj.force.domain.entity.TUser;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OpenRepository {

    private final EntityManager em;

    public boolean findIsOpen(String storeNo) {
        Long result = em.createQuery(
                "select count(*) " +
                        "from TOpenClose oc " +
                        "where oc.closeMoney is null " +
                        "and oc.tOpenCloseId.storeNo = :storeNo", Long.class)
                .setParameter("storeNo", storeNo)
                .getSingleResult();
        return result > 0;
    }

    public Optional<TOpenClose> findOpen(String storeNo) {
        List<TOpenClose> open = em.createQuery(
                        "select oc " +
                                "from TOpenClose oc " +
                                "where oc.tOpenCloseId.storeNo = :storeNo " +
                                "order by modifyId desc limit 1", TOpenClose.class)
                .setParameter("storeNo", storeNo)
                .getResultList();
        return open.stream().findAny();
    }

    public Optional<TUser> findUser(String userId) {
        List<TUser> user = em.createQuery(
                        "select u " +
                                "from TUser u " +
                                "where u.userId = :userId", TUser.class)
                .setParameter("userId", userId)
                .getResultList();
        return user.stream().findAny();
    }

    public String findOpenCloseNo(String storeNo) {
        return em.createQuery("select oc " +
                                      "from TOpenClose oc " +
                                      "where oc.tOpenCloseId.storeNo = :storeNo " +
                                      "order by oc.tOpenCloseId.openCloseNo desc limit 1", String.class)
                .setParameter("storeNo", storeNo)
                .getSingleResult();
    }


//    public String findOpenCloseSeq(String storeNo) {
//        return em.createQuery("select oc " +
//                        "from TOpenClose oc " +
//                        "where closeMoney is not null " +
//                        "and date_trunc('day', oc.insertDate) = CURRENT_DATE " +
//                        "and oc.tOpenCloseId.storeNo = :storeNo " +
//                        "order by oc.openCloseSeq desc limit 1", String.class)
//                .setParameter("storeNo", storeNo)
//                .getSingleResult();
//    }

    public int saveOpen(TOpenClose open) {
    }
}
