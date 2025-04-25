package com.main.application.service;

import com.main.application.dto.*;
import com.main.application.payload.request.EnquiryRequest;
import com.main.application.payload.request.TransactionRequest;
import com.main.application.payload.request.TransferRequest;
import com.main.application.payload.request.UserRequest;
import com.main.application.payload.response.BankResponse;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse updateAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(TransactionRequest transactionRequest);
    BankResponse debitAccount(TransactionRequest transactionRequest);
    BankResponse transferAmount(TransferRequest transferRequest);
    BankResponse login(LoginDto loginDto);
}
