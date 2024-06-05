package com.hsj.force.open.service;

import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.OpenCloseInsertDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OpenServiceTest {

    @Autowired
    private OpenService openService;
    private User loginMember = null;

    @BeforeEach
    public void beforeEach() {
        loginMember = new User();
        loginMember.setStoreNo("S001");
        loginMember.setUserId("SUSU");
        loginMember.setUserNo("U001");
        loginMember.setUserName("정현수");
        loginMember.setPhoneNum("01027287526");
        loginMember.setPassword("1234");
        loginMember.setUseYn("1");
    }

    @Test
    void selectIsOpen() {
        int result = openService.selectIsOpen(loginMember.getStoreNo());
        assertThat(result).isEqualTo(1);
    }

//    @Test
//    void selectOpenInfo() {
//        OpenCloseInsertDTO open = openService.selectOpenInfo(loginMember.getStoreNo());
//        assertThat(open).isNotNull();
//    }

//    @Test
//    void insertOpen() {
//        OpenCloseInsertDTO open = new OpenCloseInsertDTO();
//        open.setOpenMoney(10000);
//        open.setStoreNo(loginMember.getStoreNo());
//        open.setInsertId(loginMember.getUserId());
//        open.setModifyId(loginMember.getUserId());
//
//        int result = openService.insertOpen(open);
//        assertThat(result).isEqualTo(1);
//    }
}