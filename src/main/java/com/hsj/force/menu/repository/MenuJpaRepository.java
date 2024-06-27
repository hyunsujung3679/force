package com.hsj.force.menu.repository;

import com.hsj.force.domain.entity.TMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuJpaRepository extends JpaRepository<TMenu, String> {

    TMenu findFirstByOrderByMenuNoDesc();
}
