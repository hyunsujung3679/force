package com.hsj.force.login.repository;

import com.hsj.force.domain.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<User, String> {
    int findByUserIdAndPassword(String userId, String password);
}
