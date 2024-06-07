package com.hsj.force.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TINDEEREASON")
@Getter
@Setter
public class TInDeReason {

    @Id
    @Column(name = "IN_DE_REASON_NO")
    private String inDeReasonNo;

    private String inDeReason;
    private String insertId;
    private LocalDateTime insertDate;
    private String modifyId;
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "inDeReason")
    private List<TIngredientHis> ingredientHiss = new ArrayList<>();
}
