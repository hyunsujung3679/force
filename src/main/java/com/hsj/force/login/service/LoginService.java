package com.hsj.force.login.service;

import com.hsj.force.domain.vo.User;
import com.hsj.force.login.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginRepository loginRepository;
//    private final LoginQueryRepository loginQueryRepository;


    public int findByIdAndPassword(User user) {
        return loginRepository.findByUserIdAndPassword(user.getUserId(), user.getPassword());
    }
}
