package com.hsj.force.login.repository;

import com.hsj.force.domain.entity.TUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginJpaRepository extends JpaRepository<TUser, String> {

    TUser findOneByUserIdAndPassword(String userId, String password);

    TUser findOneByUserId(String userId);
}
