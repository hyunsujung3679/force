package com.hsj.force.login.service;


import com.hsj.force.domain.dto.UserDTO;
import com.hsj.force.domain.entity.TUser;
import com.hsj.force.login.repository.LoginJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginJpaRepository loginJpaRepository;

    public TUser selectUser(UserDTO user) {
        return loginJpaRepository.findOneByUserIdAndPassword(user.getUserId(), user.getPassword());
    }

}
