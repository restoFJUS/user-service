package com.restofjus.user_service.controller;

import com.restofjus.user_service.dto.RegisterRequestDto;
import com.restofjus.user_service.dto.UserResponseDto;
import com.restofjus.user_service.entity.Role;
import com.restofjus.user_service.entity.User;
import com.restofjus.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody RegisterRequestDto registerRequest) {
        User newUser = userService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getEmail(),
                registerRequest.getRoles()
        );
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<UserResponseDto> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok(new UserResponseDto(user.getUsername(), user.getPassword(), user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
