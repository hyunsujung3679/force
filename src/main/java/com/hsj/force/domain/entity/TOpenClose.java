package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.CommonData;
import com.hsj.force.domain.entity.embedded.TOpenCloseId;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "TOPENCLOSE")
@Getter
@Setter
public class TOpenClose {

    @EmbeddedId
    private TOpenCloseId openCloseId;

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
    private String insertId;
    private LocalDateTime insertDate;
    private String modifyId;
    private LocalDateTime modifyDate;
}
