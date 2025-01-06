package com.main.application.service;

import com.main.application.dto.BankResponse;
import com.main.application.dto.EnquiryRequest;
import com.main.application.dto.TransactionRequest;
import com.main.application.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(TransactionRequest transactionRequest);

    BankResponse debitAccount(TransactionRequest transactionRequest);
}
