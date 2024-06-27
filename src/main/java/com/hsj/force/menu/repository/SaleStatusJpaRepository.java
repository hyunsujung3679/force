package com.hsj.force.menu.repository;

import com.hsj.force.domain.entity.TSaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleStatusJpaRepository extends JpaRepository<TSaleStatus, String> {
    List<TSaleStatus> findAllByOrderBySaleStatusNo();
}
