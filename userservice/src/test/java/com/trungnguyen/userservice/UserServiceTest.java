package com.trungnguyen.userservice;

import com.trungnguyen.userservice.data.User;
import com.trungnguyen.userservice.data.UserRepository;
import com.trungnguyen.userservice.model.UserDTO;
import com.trungnguyen.userservice.service.UserService;
import org.apache.catalina.UserDatabase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository; // Tạo mock object UserRepository

    @Mock
    private PasswordEncoder passwordEncoder; // Tạo mock object PasswordEncoder

    @InjectMocks
    private UserService userService; // Inject các mock được đánh dấu ở trên vào UserService

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    public void initData(){
        //Giả lập instance cho User để test
        user = User.builder()
                .id(1L)
                .username("test@gmail.com")
                .password("123456")
                .employeeId("employeeID")
                .build();

        userDTO = UserDTO.builder()
                .username("test@gmail.com")
                .password("123456")
                .employeeId("employeeID")
                .build();
        //sử dụng reflection trong quá trình test, cho phép thay đổi giá trị của các trường (field) private hoặc protected mà không cần phải truy cập trực tiếp.
        //Vì userService là một field private, bạn không thể inject nó trực tiếp bằng cách bình thường.
        //Do đó, bạn sử dụng ReflectionTestUtils để inject mock của userService:
        ReflectionTestUtils.setField(userService, "userRepository", userRepository);
        ReflectionTestUtils.setField(userService, "passwordEncoder", passwordEncoder);
    }

    @Test
    void getAllUser(){
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        Assertions.assertEquals(Arrays.asList(user), userService.getAllUser());
    }

    @Test
    void saveUser(){
//        when(passwordEncoder.encode(user.getPassword())).thenReturn("123encoded");
//        user.setPassword("123encoded");
        when(userRepository.save(user)).thenReturn(user);
        Assertions.assertEquals(user, userService.saveUser(user));
    }

    @Test
    void login(){
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(true);

        var response = userService.login(user.getUsername(), user.getPassword());
        System.out.println(response);
//        Assertions.assertNotNull(userService.login(user.getUsername(),user.getPassword()));
        org.assertj.core.api.Assertions.assertThat(response.getUsername().equals(userDTO.getUsername()));

    }
}
