package com.main.application.ImplementationService;

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
    private static final String FILE = "C:\\Users\\sboya\\Downloads\\bank-statement.pdf";

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
        String userFullName = userAccount.getFirstName()+" "+userAccount.getLastName()+" ("+userAccount.getOtherName()+") ";


        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("setting size of document");

        OutputStream outputStream = new FileOutputStream(FILE);

        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("State Bank of India"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("70001, Kolkata, New Town"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell startingDate = new PdfPCell(new Phrase("Start Date: "+ startDate));
        startingDate.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End date: "+endDate));
        stopDate.setBorder(0);

        PdfPCell name = new PdfPCell(new Phrase("Customer Name: "+userFullName));
        name.setBorder(0);
        PdfPCell space = new PdfPCell();
        space.setBorder(0);
        PdfPCell address = new PdfPCell(new Phrase("Customer Address: " +userAccount.getAddress()));
        address.setBorder(0);

        PdfPTable transactionTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);
        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);
        PdfPCell amount = new PdfPCell(new Phrase("AMOUNT"));
        amount.setBackgroundColor(BaseColor.BLUE);
        amount.setBorder(0);
        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.BLUE);
        status.setBorder(0);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(amount);
        transactionTable.addCell(status);

        transactionList.forEach(transaction -> {
                    transactionTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
                    transactionTable.addCell(new Phrase(transaction.getTransactionType()));
                    transactionTable.addCell(new Phrase(transaction.getAmount().toString()));
                    transactionTable.addCell(new Phrase(transaction.getStatus()));
        });

        statementInfo.addCell(startingDate);
        statementInfo.addCell(statement);
        statementInfo.addCell(stopDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);

        document.close();
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
