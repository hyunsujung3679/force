package com.hsj.force.domain;

import lombok.Data;

@Data
public class Login {

    private String userId;      //사용자 ID
    private String userNo;      //사용자 번호
    private String storeNo;     //매장 번호
    private String password;    //비밀번호
    private String userName;    //사용자 이름
    private String phoneNum;    //핸드폰 번호
    private String useYn;       //사용 유무
}
