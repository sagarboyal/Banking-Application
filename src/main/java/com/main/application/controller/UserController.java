package com.main.application.controller;

import com.main.application.dto.*;
import com.main.application.payload.request.EnquiryRequest;
import com.main.application.payload.request.TransactionRequest;
import com.main.application.payload.request.TransferRequest;
import com.main.application.payload.request.UserRequest;
import com.main.application.payload.response.BankResponse;
import com.main.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management API")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(
            summary = "Create New User Account",
            description = "Registers a new user by accepting user details and creates a bank account with a unique account number.")
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED")
    @PostMapping("register")
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @PostMapping("login")
    public BankResponse setLogin(@RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }

    @PostMapping("update")
    public BankResponse setUpdate(@RequestBody UserRequest userRequest){
        return userService.updateAccount(userRequest);
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Retrieves the current balance of a user's bank account based on the provided account details."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS")
    @GetMapping("balance-enquiry")
    public BankResponse getBalanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.balanceEnquiry(enquiryRequest);
    }

    @Operation(
            summary = "Name Enquiry",
            description = "Retrieves the name associated with a user's bank account based on the provided account details."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("name-enquiry")
    public String getNameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return userService.nameEnquiry(enquiryRequest);
    }

    @Operation(
            summary = "Credit Amount",
            description = "Credits a specified amount to a user's bank account and updates the account balance."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping("credit")
    public BankResponse setCredit(@RequestBody TransactionRequest transactionRequest) {
        return userService.creditAccount(transactionRequest);
    }

    @Operation(
            summary = "Debit Amount",
            description = "Debits a specified amount from a user's bank account and updates the account balance."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping("debit")
    public BankResponse setDebitBankResponse(@RequestBody TransactionRequest transactionRequest) {
        return userService.debitAccount(transactionRequest);
    }


    @Operation(
            summary = "Transfer Amount",
            description = "Transfers a specified amount from the sender's bank account to the recipient's bank account and updates the respective balances."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping("transfer")
    public BankResponse setTransferRequest(@RequestBody TransferRequest transferRequest){
        return userService.transferAmount(transferRequest);
    }

}
