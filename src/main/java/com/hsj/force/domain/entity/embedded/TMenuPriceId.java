package com.hsj.force.domain.entity.embedded;

import com.hsj.force.domain.entity.TMenu;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;

import static jakarta.persistence.FetchType.LAZY;

@Data
@Embeddable
public class TMenuPriceId implements Serializable {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MENU_NO")
    private TMenu menu;

    @Column(name = "MENU_SEQ")
    private String menuSeq;
}
