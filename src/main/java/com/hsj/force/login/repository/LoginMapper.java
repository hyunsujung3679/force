package com.hsj.force.login.repository;

import com.hsj.force.domain.User;
import com.hsj.force.domain.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {

    User findUser(UserDTO user);
}
