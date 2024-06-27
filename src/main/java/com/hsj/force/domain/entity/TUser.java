package com.hsj.force.domain.entity;

import com.hsj.force.domain.entity.embedded.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "TUSER")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TUser extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "USER_NO")
    private String userNo;

    private String userId;
    private String password;
    private String userName;
    private String phoneNum;
    private String useYn;

    @Override
    public String getId() {
        return userNo;
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}
