package com.nhnacademy.mini.dooray.repository;

import com.nhnacademy.mini.dooray.domain.UserIdAndPwDto;
import com.nhnacademy.mini.dooray.domain.UserInfoDto;
import com.nhnacademy.mini.dooray.domain.UserState;
import com.nhnacademy.mini.dooray.domain.entity.User;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @Description("회원의 모든 정보를 반환")
    void testGetUsersInfo() {
        String id = "user1";
        User user = new User("user1", "사용자", "1234", "e@email.com", UserState.REGISTERED);
        entityManager.merge(user);

        Optional<User> optional = userRepository.findById(id);
        assertThat(optional).isPresent();
        assertThat(optional.get()).isEqualTo(user);
    }

    @Test
    @Description("회원의 아이디와 비밀번호만 반환 AKA 로그인")
    void testGetUsersIdAndPasswordOnly() {
        String id = "user1";
        User user = new User("user1", "사용자", "1234", "e@email.com", UserState.REGISTERED);
        entityManager.merge(user);

        UserIdAndPwDto actual = userRepository.getUserByUserId(id);
        assertThat(actual.getUserId()).isEqualTo(user.getUserId());
        assertThat(actual.getUserPassword()).isEqualTo(user.getUserPassword());
    }

    @Test
    @Description("상태와 상관없이 회원 목록 반환")
    void testGetUserList() {
        User user1 = new User("user1", "사용자1", "1234", "e1@email.com", UserState.REGISTERED);
        User user2 = new User("user2", "사용자2", "12345", "e2@email.com", UserState.INACTIVATED);
        User user3 = new User("user3", "사용자3", "123456", "e3@email.com", UserState.DEACTIVATED);
        entityManager.merge(user1);
        entityManager.merge(user2);
        entityManager.merge(user3);

        List<UserInfoDto> userList = userRepository.getAllBy();
        assertThat(userList).hasSize(3);
        for (UserInfoDto user : userList){
            assertEquals(4, Stream.of(user.getUserId(), user.getUserName(), user.getUserEmail(), user.getUserState())
                    .filter(Objects::nonNull)
                    .toList()
                    .size()
            );
        }
    }

    @Test
    @Description("상태에 따라 회원 목록 반환")
    void testGetUserListByState() {
        User user1 = new User("user1", "사용자1", "1234", "e1@email.com", UserState.REGISTERED);
        User user2 = new User("user2", "사용자2", "12345", "e2@email.com", UserState.INACTIVATED);
        User user3 = new User("user3", "사용자3", "123456", "e3@email.com", UserState.DEACTIVATED);
        entityManager.merge(user1);
        entityManager.merge(user2);
        entityManager.merge(user3);

        List<UserInfoDto> users = userRepository.getUsersByUserState(UserState.REGISTERED);
        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUserState()).isEqualTo(UserState.REGISTERED);
    }

    @Test
    @Description("회원 가입")
    void testCreateUser() {
        User user = new User("user1", "사용자1", "1234", "e1@email.com", UserState.REGISTERED);
        userRepository.save(user);

        Optional<User> actual = userRepository.findById("user1");
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(user);
    }

    @Test
    @Description("회원 정보 수정")
    void testUpdateUserInfo() {
        String id = "user1";
        User user = new User("user1", "사용자", "1234", "e@email.com", UserState.REGISTERED);
        userRepository.save(user);

        User beforeChange = new User("user1", "사용자", "1234", "e@email.com", UserState.REGISTERED);
        user.setUserName("shane");
        user.setUserEmail("b@email.com");
        user.setUserPassword("123");

        userRepository.save(user);
        Optional<User> afterChange = userRepository.findById(id);
        assertThat(beforeChange).isNotEqualTo(afterChange.get());
        assertThat(afterChange.get().getUserName()).isEqualTo("shane");
        assertThat(afterChange.get().getUserEmail()).isEqualTo("b@email.com");
        assertThat(afterChange.get().getUserPassword()).isEqualTo("123");
    }

    @Test
    @Description("회원 상태 수정")
    void testUpdateUserState() {
        String id = "user1";
        User user = new User("user1", "사용자", "1234", "e@email.com", UserState.REGISTERED);
        userRepository.save(user);

        User beforeChange = new User("user1", "사용자", "1234", "e@email.com", UserState.REGISTERED);

        user.setUserState(UserState.DEACTIVATED);

        userRepository.save(user);

        Optional<User> afterChange = userRepository.findById(id);
        assertThat(afterChange.get()).isNotEqualTo(beforeChange);
        assertThat(afterChange.get().getUserState()).isEqualTo(UserState.DEACTIVATED);
    }
}