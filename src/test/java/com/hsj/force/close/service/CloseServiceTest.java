package com.hsj.force.close.service;

import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.OpenCloseUpdateDTO;
import com.hsj.force.open.service.OpenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CloseServiceTest {

    @Autowired
    private CloseService closeService;
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
    void selectCloseInfo() {
        OpenCloseUpdateDTO close = closeService.selectCloseInfo(loginMember);
        assertThat(close).isNotNull();
    }

    @Test
    void updateOpenClose() {
        OpenCloseUpdateDTO close = new OpenCloseUpdateDTO();
        close.setOneHunThous(0);
        close.setFiftyThous(0);
        close.setTenThous(0);
        close.setFiveThous(0);
        close.setOneThous(0);
        close.setFiveHun(0);
        close.setOneHun(0);
        close.setFifty(0);
        close.setTen(0);
        close.setStoreNo(loginMember.getStoreNo());
        close.setModifyId(loginMember.getUserId());

        int result = closeService.updateOpenClose(close);
        assertThat(result).isEqualTo(1);
    }
}