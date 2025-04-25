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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management API")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/update")
    public ResponseEntity<BankResponse> setUpdate(@RequestBody UserRequest userRequest){
        return  ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateAccount(userRequest));
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Retrieves the current balance of a user's bank account based on the provided account details."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS")
    @GetMapping("/balance-enquiry")
    public ResponseEntity<BankResponse> getBalanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                        .body(userService.balanceEnquiry(enquiryRequest));
    }

    @Operation(
            summary = "Name Enquiry",
            description = "Retrieves the name associated with a user's bank account based on the provided account details."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/name-enquiry")
    public ResponseEntity<String> getNameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
        return  ResponseEntity.status(HttpStatus.FOUND)
                .body(userService.nameEnquiry(enquiryRequest));
    }

    @Operation(
            summary = "Credit Amount",
            description = "Credits a specified amount to a user's bank account and updates the account balance."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping("/credit")
    public ResponseEntity<BankResponse> setCredit(@RequestBody TransactionRequest transactionRequest) {
        return  ResponseEntity.status(HttpStatus.OK)
                .body(userService.creditAccount(transactionRequest));
    }

    @Operation(
            summary = "Debit Amount",
            description = "Debits a specified amount from a user's bank account and updates the account balance."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping("/debit")
    public ResponseEntity<BankResponse> setDebitBankResponse(@RequestBody TransactionRequest transactionRequest) {
        return  ResponseEntity.status(HttpStatus.OK)
                .body(userService.debitAccount(transactionRequest));
    }


    @Operation(
            summary = "Transfer Amount",
            description = "Transfers a specified amount from the sender's bank account to the recipient's bank account and updates the respective balances."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @PostMapping("/transfer")
    public ResponseEntity<BankResponse> setTransferRequest(@RequestBody TransferRequest transferRequest){
        return  ResponseEntity.status(HttpStatus.OK)
                .body(userService.transferAmount(transferRequest));
    }

}
