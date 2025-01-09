package com.main.application.service;

import com.itextpdf.text.DocumentException;
import com.main.application.entity.Transaction;

import java.io.FileNotFoundException;
import java.util.List;

public interface BankService {
    List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws DocumentException, FileNotFoundException;
}
