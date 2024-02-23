package com.nhnacademy.mini.dooray.service;

import com.nhnacademy.mini.dooray.domain.UserIdAndPwDto;
import com.nhnacademy.mini.dooray.domain.UserInfoDto;
import com.nhnacademy.mini.dooray.domain.UserState;
import com.nhnacademy.mini.dooray.domain.entity.User;
import com.nhnacademy.mini.dooray.domain.request.UserInfoModifyOrRegisterRequest;
import com.nhnacademy.mini.dooray.domain.request.UserStateModifyRequest;
import com.nhnacademy.mini.dooray.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(String userId){
        return userRepository.findById(userId).orElse(null);
    }

    public UserIdAndPwDto getUsersIdAndPw(String userId){
        return userRepository.getUserByUserId(userId);
    }

    public List<UserInfoDto> getUsers(){
        return userRepository.getAllBy();
    }

    public List<UserInfoDto> getUsers(UserState userState){
        return userRepository.getUsersByUserState(userState);
    }

    public void updateUserInfo(String userId, UserInfoModifyOrRegisterRequest modifyRequest){
        User user = userRepository.findById(userId).get();
        if (modifyRequest.getUserName() != null){
            user.setUserName(modifyRequest.getUserName());
        }
        if (modifyRequest.getUserEmail() != null){
            user.setUserEmail(modifyRequest.getUserEmail());
        }
        if (modifyRequest.getUserPassword() != null){
            user.setUserPassword(modifyRequest.getUserPassword());
        }

        userRepository.save(user);
    }

    public void updateUserState(String userId, UserStateModifyRequest modifyRequest){
        User user = userRepository.findById(userId).get();
        user.setUserState(modifyRequest.getUserState());

        userRepository.save(user);
    }

    public void createUser(UserInfoModifyOrRegisterRequest registerRequest){
        User user = new User(
                registerRequest.getUserId(),
                registerRequest.getUserName(),
                registerRequest.getUserPassword(),
                registerRequest.getUserEmail(),
                UserState.REGISTERED
        );

        userRepository.save(user);
    }
}
