package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "TMENU")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TMenu extends BaseEntity {

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

    @OneToMany(mappedBy = "menu")
    private List<TOrder> orders = new ArrayList<>();

    public TMenu(String menuNo) {
        this.menuNo = menuNo;
    }

    public TMenu(String menuNo, String menuName, TSaleStatus saleStatus, TCategory category, String imageSaveName, String imageOriginName, String imagePath, String imageExt) {
        this.menuNo = menuNo;
        this.menuName = menuName;
        this.saleStatus = saleStatus;
        this.category = category;
        this.imageSaveName = imageSaveName;
        this.imageOriginName = imageOriginName;
        this.imagePath = imagePath;
        this.imageExt = imageExt;
    }

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
