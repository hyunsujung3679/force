package com.hsj.force.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "TUSER")
@Getter
@Setter
public class TUser {

    @Id
    @Column(name = "USER_NO")
    private String userNo;

    private String userId;
    private String userName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "STORE_NO")
    private TStore store;

    private String password;
    private String phoneNum;
    private String useYn;
    private String insertId;
    private String insertDate;
    private String modifyId;
    private String modifyDate;

    //==연관관계 메서드==//
    public void setStore(TStore store) {
        this.store = store;
        store.getUsers().add(this);
    }
}
