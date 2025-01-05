package com.main.application.service;

import com.main.application.dto.BankResponse;
import com.main.application.dto.UserRequest;
import org.springframework.stereotype.Service;


public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
}
