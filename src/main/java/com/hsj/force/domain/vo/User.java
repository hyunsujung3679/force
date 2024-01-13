package com.hsj.force.domain.vo;

import com.hsj.force.domain.commonData;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
public class User{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userNo;
    private String storeNo;
    private String userId;
    private String password;
    private String userName;
    private String phoneNum;
    private String useYn;

}
