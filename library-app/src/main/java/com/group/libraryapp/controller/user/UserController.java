package com.group.libraryapp.controller.user;

import com.group.libraryapp.dto.user.request.UserCreateRequest;
import com.group.libraryapp.dto.user.request.UserUpdateRequest;
import com.group.libraryapp.dto.user.response.UserResponse;
import com.group.libraryapp.service.fruit.FruitService;
import com.group.libraryapp.service.user.UserServiceV1;
import com.group.libraryapp.service.user.UserServiceV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RestController
public class UserController {

    private UserServiceV2 userServiceV2;
    private FruitService fruitService;

    public UserController(UserServiceV2 userServiceV2, @Qualifier("appleService") FruitService fruitService) {
        this.userServiceV2 = userServiceV2;
        this.fruitService = fruitService;
    }

    @PostMapping("/user") //POST /user
    public void saveUser(@RequestBody UserCreateRequest request) {
        userServiceV2.saveUser(request);
    }

    @GetMapping("/user")
    public List<UserResponse> getUsers() {
        return userServiceV2.getUsers();
    }

    @PutMapping("/user")
    public void updateUser(@RequestBody UserUpdateRequest request) {
        userServiceV2.updateUser(request);
    }

    @DeleteMapping("/user")
    public void deleteUser(@RequestParam String name) {
        userServiceV2.deleteUser(name);
    }

    @GetMapping("/user/error-test")
    public void errorTest() {
        throw new IllegalArgumentException();
    }
}