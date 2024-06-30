package com.hsj.force.menu.repository;

import com.hsj.force.domain.entity.TMenuPrice;
import com.hsj.force.domain.entity.embedded.TMenuPriceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuPriceJpaRepository extends JpaRepository<TMenuPrice, TMenuPriceId>, MenuPriceJpaRepositoryCustom {
}
