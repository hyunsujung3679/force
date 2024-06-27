package com.hsj.force.table.repository;

import com.hsj.force.domain.dto.TableListDTO;
import com.hsj.force.domain.entity.TOrder;
import com.hsj.force.domain.entity.TOrderStatus;
import com.hsj.force.domain.entity.TTable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TableJpaRepository extends JpaRepository<TTable, String> {

    List<TTable> findAllByOrderByTableNo();

}
