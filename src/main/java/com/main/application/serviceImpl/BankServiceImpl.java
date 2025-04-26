package com.main.application.serviceImpl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.main.application.dto.EmailPropertiesDto;
import com.main.application.entity.Transaction;
import com.main.application.entity.User;
import com.main.application.repository.TransactionRepo;
import com.main.application.repository.UserRepo;
import com.main.application.service.BankService;
import com.main.application.service.EmailService;
import com.main.application.utils.PdfGeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EmailService emailService;
    private static final String FILE = PdfGeneratorUtil.generateFilePath();

    @Override
    public List<Transaction> generateStatement(String accountNumber,
                                               String startDate,
                                               String endDate) throws FileNotFoundException, DocumentException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if(startDate == null || startDate.isEmpty()) startDate = LocalDate.now().minusDays(7)
                .format(formatter);
        if(endDate == null || endDate.isEmpty()) endDate = LocalDate.now().format(formatter);

        // Parse start and end dates
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        // Log parsed dates
        System.out.println("Start Date: " + start.atStartOfDay());
        System.out.println("End Date: " + end.atTime(23, 59, 59));

        // Filter transactions by account number and date range
        List<Transaction> transactionList = transactionRepo.findAll()
                .stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> {
                    LocalDate transactionDate = transaction.getCreatedAt().toLocalDate();
                    return !transactionDate.isBefore(start) && !transactionDate.isAfter(end);
                })
                .toList();

        // generating bank statement pdf of user
        User userAccount = userRepo.findByAccountNumber(accountNumber);

        PdfGeneratorUtil.generateStatementPdf(
                startDate,
                endDate,
                userAccount,
                transactionList
        );

        EmailPropertiesDto email = EmailPropertiesDto.builder()
                .recipient(userAccount.getEmail())
                .subject("e-bank statements")
                .messageBody("This is your last month transactions")
                .attachment(FILE)
                .build();

        emailService.sendEmailWithAttachments(email);

        return transactionList;
    }
}
