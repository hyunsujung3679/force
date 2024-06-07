package com.hsj.force.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private String insertId;
    private LocalDateTime insertDate;
    private String modifyId;
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "saleStatus")
    private List<TMenu> menus = new ArrayList<>();
}
