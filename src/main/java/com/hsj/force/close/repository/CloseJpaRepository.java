package com.hsj.force.close.repository;

import com.hsj.force.domain.entity.TOpenClose;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CloseJpaRepository extends JpaRepository<TOpenClose, String> {

    TOpenClose findOneByCloseMoneyIsNull();
}
