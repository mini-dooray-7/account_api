package com.nhnacademy.mini.dooray.domain.request;

import com.nhnacademy.mini.dooray.domain.UserState;
import lombok.Data;

@Data
public class UserStateModifyRequest {
    private String userState;

    public UserState getUserState() {
        return UserState.valueOf(userState);
    }
}
