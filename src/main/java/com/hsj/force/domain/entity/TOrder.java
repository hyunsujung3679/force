package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.TOrderId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "TORDER")
@Getter
@Setter
public class TOrder {

    @EmbeddedId
    private TOrderId tOrderId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "TABLE_NO")
    private TTable table;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MENU_NO")
    private TMenu menu;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ORDER_STATUS_NO")
    private TOrderStatus orderStatus;

    private int salePrice;
    private int quantity;
    private int discountPrice;
    private int totalSalePrice;
    private String fullPriceYn;
    private String fullPerYn;
    private String selPriceYn;
    private String selPerYn;
    private String serviceYn;
    private LocalDateTime orderDate;
    private LocalDateTime cancelDate;
    private String insertId;
    private LocalDateTime insertDate;
    private String modifyId;
    private LocalDateTime modifyDate;

    //==연관관계 메서드==//
    public void setTable(TTable table) {
        this.table = table;
        table.getOrders().add(this);
    }

    public void setOrderStatus(TOrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        orderStatus.getOrders().add(this);
    }

    public void setMenu(TMenu menu) {
        this.menu = menu;
        menu.getOrders().add(this);
    }

}
