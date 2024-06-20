package com.hsj.force.menu.repository;

import com.hsj.force.domain.dto.MenuDTO;
import com.hsj.force.domain.entity.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MenuRepository {

    private final EntityManager em;

    public TMenu findMenu(String menuNo) {
        return em.find(TMenu.class, menuNo);
    }

    public Optional<MenuDTO> findMenuV2(String storeNo, String menuNo) {
        List<MenuDTO> menuList = em.createQuery(
                        "select new com.hsj.force.domain.dto.MenuDTO(m.menuNo, m.menuName, m.saleStatus.saleStatusNo, c.categoryNo, mp.salePrice, m.imageSaveName) " +
                                "from TMenu m " +
                                "join TCategory c on m.category.categoryNo = c.categoryNo and c.storeNo = :storeNo " +
                                "join TMenuPrice mp on mp.menuPriceId.menuNo = m.menuNo " +
                                "where m.menuNo = :menuNo " +
                                "order by mp.menuPriceId.menuSeq desc " +
                                "limit 1", MenuDTO.class)
                .setParameter("storeNo", storeNo)
                .setParameter("menuNo", menuNo)
                .getResultList();
        return menuList.stream().findAny();
    }

    public Optional<String> findMenuNo(String storeNo) {
        List<String> menuNo = em.createQuery(
                        "select m.menuNo " +
                                "from TMenu m " +
                                "join TCategory c on m.category.categoryNo = c.categoryNo " +
                                "where c.storeNo = :storeNo " +
                                "order by m.menuNo desc", String.class)
                .setParameter("storeNo", storeNo)
                .getResultList();
        return menuNo.stream().findAny();
    }

    public TSaleStatus findSaleStatus(String saleStatusNo) {
        return em.find(TSaleStatus.class, saleStatusNo);
    }

    public void saveMenu(TMenu menu) {
        em.persist(menu);
    }

    public void saveMenuIngredient(TMenuIngredient menuIngredient) {
        em.persist(menuIngredient);
    }

    public void saveMenuPrice(TMenuPrice menuPrice) {
        em.persist(menuPrice);
    }

    public MenuDTO findMenuV3(String storeNo, String menuNo) {
        return em.createQuery(
                "select new com.hsj.force.domain.dto.MenuDTO(m.menuNo, m.menuName, m.saleStatus.saleStatusNo, m.category.categoryNo, mp.salePrice, m.imageSaveName) " +
                        "from TMenu m " +
                        "join TMenuPrice mp on m.menuNo = mp.menuPriceId.menuNo " +
                        "join TCategory c on m.category.categoryNo = c.categoryNo " +
                        "where c.storeNo = : storeNo " +
                        "and m.menuNo = :menuNo " +
                        "order by mp.menuPriceId.menuSeq desc " +
                        "limit 1", MenuDTO.class)
                .setParameter("storeNo", storeNo)
                .setParameter("menuNo", menuNo)
                .getSingleResult();
    }

    public List<TMenuIngredient> findMenuIngredient(String menuNo, String storeNo) {
        return em.createQuery(
                "select mi " +
                        "from TMenuIngredient mi " +
                        "where mi.menuIngredientId.menuNo = :menuNo " +
                        "and mi.menuIngredientId.storeNo = :storeNo", TMenuIngredient.class)
                .setParameter("menuNo", menuNo)
                .setParameter("storeNo", storeNo)
                .getResultList();
    }

    public int deleteMenuIngredient(String storeNo, String menuNo) {
        return em.createQuery(
                "delete " +
                        "from TMenuIngredient mi " +
                        "where mi.menuIngredientId.storeNo = :storeNo " +
                        "and mi.menuIngredientId.menuNo = :menuNo")
                .setParameter("storeNo", storeNo)
                .setParameter("menuNo", menuNo)
                .executeUpdate();
    }

    public int findSalePrice(String menuNo) {
        return em.createQuery(
                "select mp.salePrice " +
                        "from TMenuPrice mp " +
                        "where mp.menuPriceId.menuNo = : menuNo " +
                        "order by mp.menuPriceId.menuSeq " +
                        "limit 1", Integer.class)
                .setParameter("menuNo", menuNo)
                .getSingleResult();

    }

    public Optional<String> findMenuSeq(String menuNo) {
        List<String> menuSeq = em.createQuery(
                        "select mp.menuPriceId.menuSeq " +
                                "from TMenuPrice mp " +
                                "where mp.menuPriceId.menuNo = :menuNo " +
                                "order by mp.menuPriceId.menuSeq desc " +
                                "limit 1", String.class)
                .setParameter("menuNo", menuNo)
                .getResultList();
        return menuSeq.stream().findAny();
    }

//    public List<TMenuIngredient> findMenuIngredient(String storeNo) {
//        String sql = "SELECT MI.MENU_N"
//
//        return em.createQuery("select mi " +
//                        "from TMenuIngredient mi " +
//                        "join TIngredient i " +
//                        "on mi.menuIngredientId.ingredientNo = i.ingredientId.ingredientNo " +
//                        "and ", TMenuIngredient.class)
////                .setParameter(1, storeNo)
//                .getResultList();
//    }
}
