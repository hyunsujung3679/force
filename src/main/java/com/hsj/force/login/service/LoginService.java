package com.hsj.force.login.service;

import com.hsj.force.domain.vo.User;
import com.hsj.force.login.repository.LoginMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginMapper loginMapper;

    public User findUser(User user) {
        return loginMapper.findUser(user);
    }

}
