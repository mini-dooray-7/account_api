package com.nhnacademy.mini.dooray.domain;

public enum UserState {
    // 유저
    REGISTERED{
        @Override
        public String toString() {
            return "REGISTERED";
        }
    },  // 가입
    DEACTIVATED{
        @Override
        public String toString() {
            return "DEACTIVATED";
        }
    }, //탈퇴
    INACTIVATED{
        @Override
        public String toString() {
            return "INACTIVATED";
        }
    }  // 휴면
}
