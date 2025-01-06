package com.main.application.controller;

import com.main.application.dto.BankResponse;
import com.main.application.dto.EnquiryRequest;
import com.main.application.dto.TransactionRequest;
import com.main.application.dto.UserRequest;
import com.main.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("register")
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @GetMapping("balance-enquiry")
    public BankResponse getBalanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.balanceEnquiry(enquiryRequest);
    }

    @GetMapping("name-enquiry")
    public String getNameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.nameEnquiry(enquiryRequest);
    }

    @PostMapping("credit")
    public BankResponse setCredit(@RequestBody TransactionRequest transactionRequest) {
        return userService.creditAccount(transactionRequest);
    }

    @PostMapping("debit")
    public BankResponse setDebitBankResponse(@RequestBody TransactionRequest transactionRequest) {
        return userService.debitAccount(transactionRequest);
    }

}
