package com.hsj.force.common.service;

import com.hsj.force.domain.SaleStatus;
import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.CommonLayoutDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommonServiceTest {

    @Autowired
    private CommonService commonService;
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
    void selectSaleStatusList() {
        List<SaleStatus> saleStatusList = commonService.selectSaleStatusList();
        assertThat(saleStatusList).isNotNull();
    }

    @Test
    void selectHeaderInfo() {
        CommonLayoutDTO commonLayoutDTO = commonService.selectHeaderInfo(loginMember);
        assertThat(commonLayoutDTO).isNotNull();
    }
}