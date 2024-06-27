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
@Table(name = "TCATEGORY")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TCategory extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "CATEGORY_NO")
    private String categoryNo;

    private String categoryName;
    private int priority;
    private String useYn;

    @OneToMany(mappedBy = "category")
    private List<TMenu> menus = new ArrayList<>();

    public TCategory(String categoryNo, String categoryName, int priority, String useYn) {
        this.categoryNo = categoryNo;
        this.categoryName = categoryName;
        this.priority = priority;
        this.useYn = useYn;
    }

    @Override
    public String getId() {
        return categoryNo;
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}
