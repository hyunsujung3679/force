package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.CommonData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TSALESTATUS")
@Getter
@Setter
public class TSaleStatus {

    @Id
    @Column(name = "SALE_STATUS_NO")
    private String saleStatusNo;

    private String saleStatus;

    @Embedded
    private CommonData commonData;

    @OneToMany(mappedBy = "saleStatus")
    private List<TMenu> menus = new ArrayList<>();
}
