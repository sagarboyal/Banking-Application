package com.main.application.controller;

import com.main.application.dto.LoginDto;
import com.main.application.payload.request.UserRequest;
import com.main.application.payload.response.BankResponse;
import com.main.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(
            summary = "Create New User Account",
            description = "Registers a new user by accepting user details and creates a bank account with a unique account number.")
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED")
    @PostMapping("/sign-up")
    public ResponseEntity<BankResponse> createAccount(@RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createAccount(userRequest));
    }

    @PostMapping("/log-in")
    public ResponseEntity<BankResponse> setLogin(@RequestBody LoginDto loginDto){
        return ResponseEntity.ok(userService.login(loginDto));
    }

}
