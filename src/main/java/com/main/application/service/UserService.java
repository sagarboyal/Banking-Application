package com.main.application.service;

import com.main.application.dto.*;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(TransactionRequest transactionRequest);

    BankResponse debitAccount(TransactionRequest transactionRequest);

    BankResponse transferAmount(TransferRequest transferRequest);
}
