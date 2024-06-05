package com.hsj.force.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "TTABLE")
@Getter
@Setter
public class TTable {

    @Id
    @Column(name = "TABLE_NO")
    private String tableNo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "STORE_NO")
    private TStore store;

    private String tableName;
    private String useYn;
    private String insertId;
    private String insertDate;
    private String modifyId;
    private String modifyDate;

    //==연관관계 메서드==//
    public void setStore(TStore store) {
        this.store = store;
        store.getTables().add(this);
    }
}
