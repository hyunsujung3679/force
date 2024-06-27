package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TTABLE")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TTable extends BaseEntity implements Persistable<String> {

    @Id
    private String tableNo;

    private String tableName;
    private String useYn;

    @OneToMany(mappedBy = "table")
    private List<TOrder> orders = new ArrayList<>();

    public TTable(String tableNo) {
        this.tableNo = tableNo;
    }

    @Override
    public String getId() {
        return tableNo;
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}
