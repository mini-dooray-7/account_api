package com.nhnacademy.mini.dooray.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoModifyOrRegisterRequest {
    private String userId;
    private String userName;
    private String userPassword;
    private String userEmail;
}
