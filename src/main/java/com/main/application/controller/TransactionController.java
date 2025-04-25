package com.main.application.controller;

import com.itextpdf.text.DocumentException;
import com.main.application.entity.Transaction;
import com.main.application.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private BankService bankService;

    @GetMapping("/bank-statement")
    public ResponseEntity<List<Transaction>> generateBankStatement(@RequestParam String accountNumber,
                                                                   @RequestParam(required = false) String startDate,
                                                                   @RequestParam(required = false) String endDate)
                                                                    throws DocumentException, FileNotFoundException {
        return ResponseEntity.status(HttpStatus.FOUND)
                .body(bankService.generateStatement(accountNumber, startDate, endDate));

    }
}
