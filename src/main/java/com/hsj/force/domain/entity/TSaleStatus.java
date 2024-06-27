package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TSALESTATUS")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TSaleStatus extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "SALE_STATUS_NO")
    private String saleStatusNo;

    private String saleStatus;

    @OneToMany(mappedBy = "saleStatus")
    private List<TMenu> menus = new ArrayList<>();

    @Override
    public String getId() {
        return saleStatusNo;
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}
