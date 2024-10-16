package com.trungnguyen.userservice;

import com.trungnguyen.userservice.controller.UserController;
import com.trungnguyen.userservice.data.User;
import com.trungnguyen.userservice.model.UserDTO;
import com.trungnguyen.userservice.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class) //thông báo JUnit rằng bạn muốn sử dụng các extension
public class UserControllerTest {

    @Mock
    private UserService userService; // Tạo mock object

    @InjectMocks // Inject các mock vào UserController
    private UserController userController;

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
        ReflectionTestUtils.setField(userController,"userService",userService);
    }

    @Test
    void getAllUser(){
        List<User> listUser = new ArrayList<>();
        listUser.add(user);
        //giả lập hành vi của một phương thức
        when(userService.getAllUser()).thenReturn(listUser);
        // Đây là phương thức của JUnit để kiểm tra tính đúng đắn của kết quả.
        //Kỳ vọng rằng userController.getAllUser() sẽ trả về đúng danh sách listUser
        Assertions.assertEquals(listUser, userController.getAllUser());
    }

    @Test
    void addUser(){
        when(userService.saveUser(user)).thenReturn(user);
        Assertions.assertEquals(user, userController.addUser(user));
    }

    @Test
    void login(){
        when(userService.login(anyString(), anyString())).thenReturn(userDTO);
        Assertions.assertEquals(userDTO, userController.login(userDTO));
    }
}
