package com.hsj.force.login.repository;



import com.hsj.force.domain.entity.TUser;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LoginRepository {

    private final EntityManager em;

    public TUser findUser(String userId, String password) {
        return em.createQuery(
                "select u " +
                        "from TUser u " +
                        "join fetch u.store " +
                        "where u.userId = :userId " +
                        "and u.password = :password", TUser.class)
                .setParameter("userId", userId)
                .setParameter("password", password)
                .getSingleResult();
    }
}
