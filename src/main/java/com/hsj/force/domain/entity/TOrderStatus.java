package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.CommonData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Embedded
    private CommonData commonData;

    @OneToMany(mappedBy = "orderStatus")
    private List<TOrder> orders = new ArrayList<>();
}
