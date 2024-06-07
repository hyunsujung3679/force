package com.hsj.force.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "TMENU")
@Getter
@Setter
public class TMenu {

    @Id
    @Column(name = "MENU_NO")
    private String menuNo;

    private String menuName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "SALE_STATUS_NO")
    private TSaleStatus saleStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "CATEGORY_NO")
    private TCategory category;

    private String imageSaveName;
    private String imageOriginName;
    private String imagePath;
    private String imageExt;
    private String insertId;
    private LocalDateTime insertDate;
    private String modifyId;
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "menu")
    private List<TOrder> orders = new ArrayList<>();

    //==연관관계 메서드==//
    public void setSaleStatus(TSaleStatus saleStatus) {
        this.saleStatus = saleStatus;
        saleStatus.getMenus().add(this);
    }

    public void setCategory(TCategory category) {
        this.category = category;
        category.getMenus().add(this);
    }
}
