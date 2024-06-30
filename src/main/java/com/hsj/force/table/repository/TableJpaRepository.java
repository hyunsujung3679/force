package com.hsj.force.table.repository;

import com.hsj.force.domain.entity.TTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TableJpaRepository extends JpaRepository<TTable, String>, TableJpaRepositoryCustom {

    List<TTable> findAllByOrderByTableNo();

}
