package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.BaseEntity;
import com.hsj.force.domain.entity.embedded.TOrderId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "TORDER")
@IdClass(TOrderId.class)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TOrder extends BaseEntity implements Persistable<TOrderId> {

    @Id
    @Column(name = "ORDER_NO")
    private String orderNo;

    @Id
    @Column(name = "ORDER_SEQ")
    private String orderSeq;

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

    public TOrder(String orderNo, String orderSeq, TTable table, TMenu menu, TOrderStatus orderStatus, int salePrice, int quantity, int discountPrice, int totalSalePrice, String fullPriceYn, String fullPerYn, String selPriceYn, String selPerYn, String serviceYn, LocalDateTime orderDate, LocalDateTime cancelDate) {
        this.orderNo = orderNo;
        this.orderSeq = orderSeq;
        if(table != null ) setTable(table);
        if(menu != null ) setMenu(menu);
        if(orderStatus != null) setOrderStatus(orderStatus);
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.discountPrice = discountPrice;
        this.totalSalePrice = totalSalePrice;
        this.fullPriceYn = fullPriceYn;
        this.fullPerYn = fullPerYn;
        this.selPriceYn = selPriceYn;
        this.selPerYn = selPerYn;
        this.serviceYn = serviceYn;
        this.orderDate = orderDate;
        this.cancelDate = cancelDate;
    }

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

    @Override
    public TOrderId getId() {
        return new TOrderId(orderNo, orderSeq);
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}
