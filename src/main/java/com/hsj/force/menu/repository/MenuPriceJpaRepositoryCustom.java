package com.hsj.force.menu.repository;

import com.hsj.force.domain.dto.MenuPriceDTO;

public interface MenuPriceJpaRepositoryCustom {

    MenuPriceDTO findMenuPriceDTO(String menuNo);

}
