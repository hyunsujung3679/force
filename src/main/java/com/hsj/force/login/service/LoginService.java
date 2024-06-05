package com.hsj.force.login.service;


import com.hsj.force.domain.dto.UserDTO;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.login.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginRepository loginRepository;

    public TUser findUser(UserDTO user) {
        return loginRepository.findUser(user.getUserId(), user.getPassword());
    }

}
