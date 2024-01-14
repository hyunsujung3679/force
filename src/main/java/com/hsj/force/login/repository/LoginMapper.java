package com.hsj.force.login.repository;

import com.hsj.force.domain.vo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {

    User findUser(User user);
}
