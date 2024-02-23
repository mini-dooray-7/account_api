package com.nhnacademy.mini.dooray.controller;

import com.nhnacademy.mini.dooray.domain.UserIdAndPwDto;
import com.nhnacademy.mini.dooray.domain.UserInfoDto;
import com.nhnacademy.mini.dooray.domain.UserState;
import com.nhnacademy.mini.dooray.domain.entity.User;
import com.nhnacademy.mini.dooray.domain.request.UserInfoModifyOrRegisterRequest;
import com.nhnacademy.mini.dooray.domain.request.UserStateModifyRequest;
import com.nhnacademy.mini.dooray.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") String id) {
        return userService.getUser(id);
    }

    @GetMapping("/auth/login/{id}")
    public UserIdAndPwDto login(@PathVariable("id") String id) {
        return userService.getUsersIdAndPw(id);
    }

    @GetMapping("/users")
    public List<UserInfoDto> getUsersByState(@RequestParam(name = "state", required = false) String state) {
        if (state != null) {
            UserState userState = UserState.valueOf(state);
            return userService.getUsers(userState);
        }
        return userService.getUsers();
    }

    @PutMapping("/users/info/{id}")
    public void updateUserInfo(@PathVariable("id") String id, @RequestBody UserInfoModifyOrRegisterRequest modifyRequest) {
        userService.updateUserInfo(id, modifyRequest);
    }

    @PutMapping("/users/state/{id}")
    public void updateUserState(@PathVariable("id") String id, @RequestBody UserStateModifyRequest modifyRequest) {
        userService.updateUserState(id, modifyRequest);
    }

    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody UserInfoModifyOrRegisterRequest registerRequest) {
        userService.createUser(registerRequest);
    }
}
