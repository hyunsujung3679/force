package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.CommonData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TCATEGORY")
@Getter
@Setter
public class TCategory {

    @Id
    @Column(name = "CATEGORY_NO")
    private String categoryNo;

    private String storeNo;
    private String categoryName;
    private int priority;
    private String useYn;
    private String insertId;
    private LocalDateTime insertDate;
    private String modifyId;
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "category")
    private List<TMenu> menus = new ArrayList<>();
}
