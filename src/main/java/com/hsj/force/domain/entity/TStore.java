package com.hsj.force.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TSTORE")
@Setter
@Getter
public class TStore {

    @Id
    @Column(name = "STORE_NO")
    private String storeNo;

    private String storeName;
    private String useYn;
    private String insertId;
    private String insertDate;
    private String modifyId;
    private String modifyDate;

    @OneToMany(mappedBy = "store")
    private List<TUser> users = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<TTable> tables = new ArrayList<>();

}
