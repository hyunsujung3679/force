//package com.hsj.force.order.service;
//
//import com.hsj.force.domain.dto.OrderListDTO;
//import com.hsj.force.domain.dto.OrderSaveDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@Transactional
//class OrderServiceTest {
//
//    @Autowired
//    private OrderService orderService;
//    private User loginMember = null;
//
//    @BeforeEach
//    public void beforeEach() {
//        loginMember = new User();
//        loginMember.setStoreNo("S001");
//        loginMember.setUserId("SUSU");
//        loginMember.setUserNo("U001");
//        loginMember.setUserName("정현수");
//        loginMember.setPhoneNum("01027287526");
//        loginMember.setPassword("1234");
//        loginMember.setUseYn("1");
//    }
//
//    @Test
//    void selectOrderInfo() {
//        Map<String, Object> map = orderService.selectOrderInfo(loginMember, "T002");
//        assertThat(map.get("commonLayoutForm")).isNotNull();
//        assertThat(map.get("categoryList")).isNotNull();
//        assertThat(map.get("menuList")).isNotNull();
//        assertThat(map.get("orderList")).isNotNull();
//        assertThat(map.get("orderTotal")).isNotNull();
//    }
//
//    @Test
//    void saveOrder() {
//        OrderSaveDTO order = new OrderSaveDTO();
//        order.setTableNo("T002");
//        order.setMenuNo("M001");
//        int result = orderService.saveOrder(loginMember, order);
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void selectOrderList() {
//        List<OrderListDTO> orderListList = orderService.selectOrderList(loginMember.getStoreNo(), "T002");
//        assertThat(orderListList).isNotNull();
//    }
//
//    @Test
//    void completeOrder() {
//        int result = orderService.completeOrder(loginMember, "T002");
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void cancelSelection() {
//        OrderSaveDTO order = new OrderSaveDTO();
//        order.setOrderNo("O005");
//        order.setMenuNo("M001");
//        order.setTableNo("T002");
//        int result = orderService.cancelSelection(loginMember, order);
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void cancelWhole() {
//        OrderSaveDTO order = new OrderSaveDTO();
//        order.setOrderNo("O005");
//        order.setTableNo("T002");
//        int result = orderService.cancelWhole(loginMember, order);
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void changeQuantity() {
//        OrderSaveDTO order = new OrderSaveDTO();
//        order.setOrderNo("O005");
//        order.setMenuNo("M001");
//        order.setTableNo("T002");
//        order.setQuantity("10");
//        int result = orderService.changeQuantity(loginMember, order);
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void changeSalePrice() {
//        OrderSaveDTO order = new OrderSaveDTO();
//        order.setOrderNo("O005");
//        order.setMenuNo("M001");
//        order.setTableNo("T002");
//        order.setSalePrice("50,000");
//        int result = orderService.changeSalePrice(loginMember, order);
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void service() {
//        OrderSaveDTO order = new OrderSaveDTO();
//        order.setOrderNo("O005");
//        order.setMenuNo("M001");
//        order.setTableNo("T002");
//        int result = orderService.service(loginMember, order);
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void discountFullPer() {
//        OrderSaveDTO order = new OrderSaveDTO();
//        order.setOrderNo("O005");
//        order.setMenuNo("M001");
//        order.setTableNo("T002");
//        order.setPercent("10");
//        int result = orderService.discountFullPer(loginMember, order);
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void discountFullPrice() {
//        OrderSaveDTO order = new OrderSaveDTO();
//        order.setOrderNo("O005");
//        order.setTableNo("T002");
//        order.setDiscountPrice("30,000");
//        int result = orderService.discountFullPrice(loginMember, order);
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void discountFullCancel() {
//        OrderSaveDTO order = new OrderSaveDTO();
//        order.setOrderNo("O005");
//        order.setTableNo("T002");
//        int result = orderService.discountFullCancel(loginMember, order);
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void discountSelPer() {
//        OrderSaveDTO order = new OrderSaveDTO();
//        order.setOrderNo("O005");
//        order.setMenuNo("M001");
//        order.setTableNo("T002");
//        order.setPercent("10");
//        int result = orderService.discountSelPer(loginMember, order);
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void discountSelPrice() {
//        OrderSaveDTO order = new OrderSaveDTO();
//        order.setOrderNo("O005");
//        order.setMenuNo("M001");
//        order.setTableNo("T002");
//        order.setDiscountPrice("30,000");
//        int result = orderService.discountSelPrice(loginMember, order);
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void discountSelCancel() {
//        OrderSaveDTO order = new OrderSaveDTO();
//        order.setOrderNo("O005");
//        order.setMenuNo("M001");
//        order.setTableNo("T002");
//        int result = orderService.discountSelCancel(loginMember, order);
//        assertThat(result).isEqualTo(1);
//    }
//
//    @Test
//    void checkStock() {
//        boolean isEnoughStock = orderService.checkStock(loginMember.getStoreNo(), "M001");
//        assertThat(isEnoughStock).isEqualTo(true);
//    }
//}