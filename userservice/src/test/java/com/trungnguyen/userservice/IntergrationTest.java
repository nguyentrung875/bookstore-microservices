package com.trungnguyen.userservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trungnguyen.userservice.data.User;
import com.trungnguyen.userservice.data.UserRepository;
import com.trungnguyen.userservice.model.UserDTO;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "test.properties")
public class IntergrationTest {

    // Inject port ngẫu nhiên mà Spring Boot chọn để chạy ứng dụng trong test này.
    @LocalServerPort
    private int port;

    private static RestTemplate restTemplate; //công cụ gọi rest API

    //thay vì sử dụng repository thật, giả lập repository mà không cần kết nối tới cơ sở dữ liệu.
    @MockBean
    UserRepository userRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    private Gson gson = new Gson();

    private User user;
    private UserDTO userDTO;
    private String baseUrl = "http://localhost";

    @BeforeAll //sẽ chạy trước tất cả mọi thứ kể cả testcase
    public static void init(){
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void setUp(){
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
        baseUrl = baseUrl.concat(":").concat(port+"").concat("/api/v1/users");
    }

    @Test
    void ShouldGetAllUser(){
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);
        //Gửi request đến endpoint /listUser và nhận một ResponseEntity chứa list user
        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl.concat("/listUser"),List.class);
        System.out.println(gson.toJson(response.getBody()));
        System.out.println(response.getStatusCode());

        //Kiểm tra kết quả api trả về với list user giả lập
        Assertions.assertEquals(gson.toJson(users),gson.toJson(response.getBody()));

        //Kiểm tra HttpStatus code có phải là 200 OK hay không
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void login(){
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(user.getPassword(), user.getPassword())).thenReturn(true);
        ResponseEntity<UserDTO> response = restTemplate.postForEntity(baseUrl.concat("/login"),userDTO,UserDTO.class);

        System.out.println(gson.toJson(response.getBody()));
        System.out.println(response.getStatusCode());

        Assertions.assertEquals(userDTO.getUsername(),response.getBody().getUsername());
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());

    }

    @Test
    void login_with_invalid_password(){
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(user.getPassword(), userDTO.getPassword())).thenReturn(false); // Giả lập password không khớp

        ResponseEntity<UserDTO> response = restTemplate.postForEntity(baseUrl.concat("/login"),userDTO,UserDTO.class);

        // Kiểm tra kết quả: trong trường hợp này, token không nên được tạo ra
        Assertions.assertNull(response.getBody().getToken());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

//    @Test
//    void login_user_not_found(){
//        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty()); // Giả lập user không tồn tại
//
//        // Thực hiện gọi API
//        ResponseEntity<UserDTO> response = restTemplate.postForEntity(baseUrl.concat("/login"),userDTO,UserDTO.class);
//
//        // Kiểm tra xem xử lý thế nào khi không tìm thấy user, có thể mong đợi 1 Exception hoặc DTO không có thông tin
//        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }

}
