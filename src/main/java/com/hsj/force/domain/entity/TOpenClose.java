package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "TOPENCLOSE")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TOpenClose extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "OPEN_CLOSE_NO")
    private String openCloseNo;

    private String openCloseSeq;
    private int openMoney;
    private int oneHunThous;
    private int fiftyThous;
    private int tenThous;
    private int fiveThous;
    private int oneThous;
    private int fiveHun;
    private int oneHun;
    private int fifty;
    private int ten;
    private Integer closeMoney;

    public TOpenClose(String openCloseNo, String openCloseSeq, int openMoney, Integer closeMoney) {
        this.openCloseNo = openCloseNo;
        this.openCloseSeq = openCloseSeq;
        this.openMoney = openMoney;
        this.closeMoney = closeMoney;
    }

    @Override
    public String getId() {
        return openCloseNo;
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}
