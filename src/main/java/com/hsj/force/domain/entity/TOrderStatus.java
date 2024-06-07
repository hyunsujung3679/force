package com.hsj.force.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TORDERSTATUS")
@Getter
@Setter
public class TOrderStatus {

    @Id
    @Column(name = "ORDER_STATUS_NO")
    private String orderStatusNo;

    private String orderStatus;
    private String insertId;
    private LocalDateTime insertDate;
    private String modifyID;
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "orderStatus")
    private List<TOrder> orders = new ArrayList<>();
}
