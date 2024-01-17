package com.hsj.force.domain;

import com.hsj.force.domain.CommonData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class User {

    private String userId;
    private String userNo;
    private String storeNo;
    private String password;
    private String userName;
    private String phoneNum;
    private String useYn;
    public User() {
    }

    public User(String userId, String userNo, String storeNo, String password, String userName, String phoneNum, String useYn) {
        this.userId = userId;
        this.userNo = userNo;
        this.storeNo = storeNo;
        this.password = password;
        this.userName = userName;
        this.phoneNum = phoneNum;
        this.useYn = useYn;
    }
}
