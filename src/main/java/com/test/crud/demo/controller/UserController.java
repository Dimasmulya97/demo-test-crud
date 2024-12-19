package com.test.crud.demo.controller;

import com.test.crud.demo.dto.LoginRequest;
import com.test.crud.demo.dto.UserRequest;
import com.test.crud.demo.service.ProductServices;
import com.test.crud.demo.service.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServices userServices;

    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@Validated @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userServices.register(userRequest));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@Validated @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userServices.login(loginRequest));
    }
}
