package com.savemystudies.backend.controller;

import com.savemystudies.backend.dto.UserLoginDto;
import com.savemystudies.backend.dto.UserRegistrationDto;
import com.savemystudies.backend.model.User;
import com.savemystudies.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationDto userDto) {
        User newUser = userService.registerNewUser(userDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@Valid @RequestBody UserLoginDto loginDto) {
        User authenticatedUser = userService.authenticateUser(loginDto);
        return ResponseEntity.ok(authenticatedUser);
    }
}