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
@Table(name = "TINDEREASON")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TInDeReason extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "IN_DE_REASON_NO")
    private String inDeReasonNo;

    private String inDeReason;

    @OneToMany(mappedBy = "inDeReason")
    private List<TIngredientHis> ingredientHiss = new ArrayList<>();

    @Override
    public String getId() {
        return inDeReasonNo;
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}
