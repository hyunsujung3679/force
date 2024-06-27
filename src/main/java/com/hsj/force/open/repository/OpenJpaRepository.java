package com.hsj.force.open.repository;

import com.hsj.force.domain.entity.TOpenClose;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenJpaRepository extends JpaRepository<TOpenClose, String> {

    long countByCloseMoneyIsNull();

    TOpenClose findFirstByOrderByModifyDateDesc();

    TOpenClose findFirstByOrderByOpenCloseNoDesc();
}
