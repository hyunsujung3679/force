package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.CommonData;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "STORE_NO")
    private TStore store;

    private String userId;
    private String password;
    private String userName;
    private String phoneNum;
    private String useYn;

    @Embedded
    private CommonData commonData;

    //==연관관계 메서드==//
    public void setStore(TStore store) {
        this.store = store;
        store.getUsers().add(this);
    }
}
