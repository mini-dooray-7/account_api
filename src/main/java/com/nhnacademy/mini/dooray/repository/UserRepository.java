package com.nhnacademy.mini.dooray.repository;

import com.nhnacademy.mini.dooray.domain.UserIdAndPwDto;
import com.nhnacademy.mini.dooray.domain.UserInfoDto;
import com.nhnacademy.mini.dooray.domain.UserState;
import com.nhnacademy.mini.dooray.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<UserInfoDto> getAllBy();
    List<UserInfoDto> getUsersByUserState(UserState userState);
    UserIdAndPwDto getUserByUserId(String userId);
}
