package com.hsj.force.order.repository;

import com.hsj.force.domain.entity.TOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusJpaRepository extends JpaRepository<TOrderStatus, String> {
}
