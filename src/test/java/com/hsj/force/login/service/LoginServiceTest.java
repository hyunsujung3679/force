package com.hsj.force.login.service;

import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LoginServiceTest {

    @Autowired
    public LoginService loginService;

    @Test
    void findUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId("SUSU");
        userDTO.setPassword("1234");
        User loginMember = loginService.findUser(userDTO);
        assertThat(loginMember).isNotNull();
    }
}