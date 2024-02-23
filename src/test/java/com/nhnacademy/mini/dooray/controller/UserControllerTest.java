package com.nhnacademy.mini.dooray.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.mini.dooray.domain.UserIdAndPwDto;
import com.nhnacademy.mini.dooray.domain.UserState;
import com.nhnacademy.mini.dooray.domain.entity.User;
import com.nhnacademy.mini.dooray.domain.request.UserInfoModifyOrRegisterRequest;
import com.nhnacademy.mini.dooray.domain.request.UserStateModifyRequest;
import com.nhnacademy.mini.dooray.repository.UserRepository;
import com.nhnacademy.mini.dooray.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @Order(1)
    void getUser() throws Exception {
        UserInfoModifyOrRegisterRequest registerRequest = new UserInfoModifyOrRegisterRequest("user1", "사용자1", "1234","e1@email.com");
        userService.createUser(registerRequest);
        mockMvc.perform(get("/users/{id}", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName", equalTo("사용자1")));
    }

    @Test
    @Order(2)
    void login() throws Exception {
        UserInfoModifyOrRegisterRequest registerRequest = new UserInfoModifyOrRegisterRequest("user1", "사용자1", "1234","e1@email.com");
        userService.createUser(registerRequest);

        mockMvc.perform(get("/auth/login/{id}", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", equalTo("user1")))
                .andExpect(jsonPath("$.userPassword", equalTo("1234")));
    }

    @Test
    @Order(3)
    void getUsersByState() throws Exception {
        UserInfoModifyOrRegisterRequest user1 = new UserInfoModifyOrRegisterRequest("user1", "사용자1", "1234","e1@email.com");
        UserInfoModifyOrRegisterRequest user2 = new UserInfoModifyOrRegisterRequest("user2", "사용자2", "1234","e2@email.com");
        UserInfoModifyOrRegisterRequest user3 = new UserInfoModifyOrRegisterRequest("user3", "사용자3", "1234","e3@email.com");
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId", equalTo("user1")))
                .andExpect(jsonPath("$[0].userState", equalTo(UserState.REGISTERED.toString())))
                .andExpect(jsonPath("$[1].userId", equalTo("user2")))
                .andExpect(jsonPath("$[0].userState", equalTo(UserState.REGISTERED.toString())))
                .andExpect(jsonPath("$[2].userId", equalTo("user3")))
                .andExpect(jsonPath("$[0].userState", equalTo(UserState.REGISTERED.toString())));
    }

    @Test
    @Order(4)
    void updateUserInfo() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String id = "user1";
        UserInfoModifyOrRegisterRequest registerRequest = new UserInfoModifyOrRegisterRequest("user1", "사용자1", "1234","e1@email.com");
        userService.createUser(registerRequest);

        UserInfoModifyOrRegisterRequest modifyRequest = new UserInfoModifyOrRegisterRequest();
        modifyRequest.setUserName("shane");
        modifyRequest.setUserPassword("0000");

        mockMvc.perform(put("/users/info/{id}", id)
                        .content(objectMapper.writeValueAsString(modifyRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void updateUserState() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String id = "user1";
        UserInfoModifyOrRegisterRequest registerRequest = new UserInfoModifyOrRegisterRequest("user1", "사용자1", "1234","e1@email.com");
        userService.createUser(registerRequest);

        UserStateModifyRequest modifyRequest = new UserStateModifyRequest();
        modifyRequest.setUserState(UserState.DEACTIVATED.toString());

        mockMvc.perform(put("/users/state/{id}", id)
                        .content(objectMapper.writeValueAsString(modifyRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void registerUser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserInfoModifyOrRegisterRequest registerRequest = new UserInfoModifyOrRegisterRequest();
        registerRequest.setUserId("user2");
        registerRequest.setUserName("nana");
        registerRequest.setUserEmail("nana@email.com");
        registerRequest.setUserPassword("54321");

        mockMvc.perform(post("/auth/register")
                        .content(objectMapper.writeValueAsString(registerRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated());
    }
}