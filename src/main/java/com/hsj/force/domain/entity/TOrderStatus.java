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
@Table(name = "TORDERSTATUS")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TOrderStatus extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "ORDER_STATUS_NO")
    private String orderStatusNo;

    private String orderStatus;

    @OneToMany(mappedBy = "orderStatus")
    private List<TOrder> orders = new ArrayList<>();

    public TOrderStatus(String orderStatusNo) {
        this.orderStatusNo = orderStatusNo;
    }

    @Override
    public String getId() {
        return orderStatusNo;
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}
