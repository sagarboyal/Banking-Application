package com.main.application.service;

import com.main.application.dto.TransactionDto;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
