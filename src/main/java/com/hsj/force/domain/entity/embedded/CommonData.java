package com.hsj.force.domain.entity.embedded;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDateTime;

@Embeddable
@Getter
public class CommonData {

    private String insertId;
    private LocalDateTime insertDate;
    private String modifyId;
    private LocalDateTime modifyDate;

    public CommonData() {}

    public CommonData(String modifyId, LocalDateTime modifyDate) {
        this.modifyId = modifyId;
        this.modifyDate = modifyDate;
    }

    public CommonData(String insertId, LocalDateTime insertDate, String modifyId, LocalDateTime modifyDate) {
        this.insertId = insertId;
        this.insertDate = insertDate;
        this.modifyId = modifyId;
        this.modifyDate = modifyDate;
    }
}
