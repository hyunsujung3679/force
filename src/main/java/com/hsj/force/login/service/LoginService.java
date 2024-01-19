package com.hsj.force.login.service;

import com.hsj.force.domain.Login;
import com.hsj.force.domain.LoginForm;
import com.hsj.force.login.repository.LoginMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginMapper loginMapper;

    public Login findUser(LoginForm loginForm) {
        return loginMapper.findUser(loginForm);
    }

}
