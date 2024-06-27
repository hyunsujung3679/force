package com.hsj.force.domain.dto;

import com.hsj.force.domain.entity.TOrder;
import lombok.Data;

@Data
public class OrderListDTO {

    private String orderNo;
    private String orderSeq;
    private String menuNo;
    private String menuName;
    private Integer salePrice;
    private Integer quantity;
    private Integer discountPrice;
    private Integer totalSalePrice;
    private String fullPriceYn;
    private String fullPerYn;
    private String selPriceYn;
    private String selPerYn;
    private String serviceYn;
    private String no;
    private String etc;
    private String tableNo;
    private String orderStatusNo;

    public OrderListDTO(String orderNo, String orderSeq, String menuNo, String menuName, Integer salePrice, Integer quantity, Integer discountPrice, Integer totalSalePrice, String fullPriceYn, String fullPerYn, String selPriceYn, String selPerYn, String serviceYn, String no, String etc, String tableNo, String orderStatusNo) {
        this.orderNo = orderNo;
        this.orderSeq = orderSeq;
        this.menuNo = menuNo;
        this.menuName = menuName;
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.discountPrice = discountPrice;
        this.totalSalePrice = totalSalePrice;
        this.fullPriceYn = fullPriceYn;
        this.fullPerYn = fullPerYn;
        this.selPriceYn = selPriceYn;
        this.selPerYn = selPerYn;
        this.serviceYn = serviceYn;
        this.no = no;
        this.etc = etc;
        this.tableNo = tableNo;
        this.orderStatusNo = orderStatusNo;
    }

    public OrderListDTO(String orderNo, String orderSeq, String menuNo, String menuName, Integer salePrice, Integer quantity, Integer discountPrice, Integer totalSalePrice, String fullPriceYn, String fullPerYn, String selPriceYn, String selPerYn, String serviceYn, String tableNo, String orderStatusNo) {
        this.orderNo = orderNo;
        this.orderSeq = orderSeq;
        this.menuNo = menuNo;
        this.menuName = menuName;
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.discountPrice = discountPrice;
        this.totalSalePrice = totalSalePrice;
        this.fullPriceYn = fullPriceYn;
        this.fullPerYn = fullPerYn;
        this.selPriceYn = selPriceYn;
        this.selPerYn = selPerYn;
        this.serviceYn = serviceYn;
        this.no = no;
        this.etc = etc;
        this.tableNo = tableNo;
        this.orderStatusNo = orderStatusNo;
    }

    public OrderListDTO (TOrder order) {
        orderNo = order.getOrderNo();
        orderSeq = order.getOrderSeq();
        menuNo = order.getMenu().getMenuNo();
        menuName = order.getMenu().getMenuName();
        salePrice = order.getSalePrice();
        quantity = order.getQuantity();
        discountPrice = order.getDiscountPrice();
        totalSalePrice = order.getTotalSalePrice();
        fullPriceYn = order.getFullPriceYn();
        fullPerYn = order.getFullPerYn();
        selPriceYn = order.getSelPriceYn();
        selPerYn = order.getSelPerYn();
        serviceYn = order.getServiceYn();
        tableNo = order.getTable().getTableNo();
        orderStatusNo = order.getOrderStatus().getOrderStatusNo();
    }

}
