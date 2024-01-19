package com.hsj.force.login.repository;

import com.hsj.force.domain.Login;
import com.hsj.force.domain.LoginForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {

    Login findUser(LoginForm loginForm);
}
