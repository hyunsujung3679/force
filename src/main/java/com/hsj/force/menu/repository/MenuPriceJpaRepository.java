package com.hsj.force.menu.repository;

import com.hsj.force.domain.dto.MenuPriceDTO;
import com.hsj.force.domain.entity.TMenuPrice;
import com.hsj.force.domain.entity.embedded.TMenuPriceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuPriceJpaRepository extends JpaRepository<TMenuPrice, TMenuPriceId> {

    @Query("select new com.hsj.force.domain.dto.MenuPriceDTO(mp.menuNo, mp.menuSeq, m.menuName, m.saleStatus.saleStatusNo, m.category.categoryNo, mp.salePrice, m.imageSaveName) " +
            "from TMenuPrice mp " +
            "join TMenu m on mp.menuNo = m.menuNo " +
            "where mp.menuNo = :menuNo " +
            "order by mp.menuSeq desc " +
            "limit 1")
    MenuPriceDTO findMenuPriceDTO(@Param("menuNo") String menuNo);

}
