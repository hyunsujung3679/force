package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.TTableId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TTABLE")
@IdClass(TTableId.class)
@Getter
@Setter
public class TTable {

//    @EmbeddedId
//    private TTableId tableId;

    @Id
    private String tableNo;

    @Id
    private String storeNo;

    private String tableName;
    private String useYn;
    private String insertId;
    private String insertDate;
    private String modifyId;
    private String modifyDate;

    @OneToMany(mappedBy = "table")
    private List<TOrder> orders = new ArrayList<>();

}
