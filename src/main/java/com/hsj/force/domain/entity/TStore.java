package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.CommonData;
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

    @Embedded
    private CommonData commonData;

    @OneToMany(mappedBy = "store")
    private List<TUser> users = new ArrayList<>();

//    @OneToMany(mappedBy = "store")
//    private List<TTable> tables = new ArrayList<>();

}
