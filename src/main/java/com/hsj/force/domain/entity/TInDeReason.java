package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.CommonData;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TINDEREASON")
@Getter
@Setter
public class TInDeReason {

    @Id
    @Column(name = "IN_DE_REASON_NO")
    private String inDeReasonNo;

    private String inDeReason;

    @Embedded
    private CommonData commonData;

    @OneToMany(mappedBy = "inDeReason")
    private List<TIngredientHis> ingredientHiss = new ArrayList<>();
}
