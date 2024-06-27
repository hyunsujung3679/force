package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.BaseEntity;
import com.hsj.force.domain.entity.embedded.TMenuPriceId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "TMENUPRICE")
@IdClass(TMenuPriceId.class)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TMenuPrice extends BaseEntity implements Persistable<TMenuPriceId> {

    @Id
    @Column(name = "MENU_NO")
    private String menuNo;

    @Id
    @Column(name = "MENU_SEQ")
    private String menuSeq;

    private int salePrice;

    public TMenuPrice(String menuNo, String menuSeq, int salePrice) {
        this.menuNo = menuNo;
        this.menuSeq = menuSeq;
        this.salePrice = salePrice;
    }

    @Override
    public TMenuPriceId getId() {
        return new TMenuPriceId(menuNo, menuSeq);
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}
