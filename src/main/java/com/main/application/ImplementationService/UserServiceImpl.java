package com.main.application.ImplementationService;

import com.main.application.dto.AccountInfo;
import com.main.application.dto.BankResponse;
import com.main.application.dto.EmailProperties;
import com.main.application.dto.EnquiryRequest;
import com.main.application.dto.TransactionRequest;
import com.main.application.dto.UserRequest;
import com.main.application.entity.User;
import com.main.application.repository.UserRepo;
import com.main.application.service.EmailService;
import com.main.application.service.UserService;
import com.main.application.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {
        @Autowired
        private UserRepo userRepo;
        @Autowired
        private EmailService emailService;

        @Override
        public BankResponse createAccount(UserRequest userRequest) {

                if (userRepo.existsByEmail(userRequest.getEmail()))
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                                        .accountInfo(null)
                                        .build();

                User newUser = User.builder()
                                .firstName(userRequest.getFirstName())
                                .lastName(userRequest.getLastName())
                                .otherName(userRequest.getOtherName())
                                .gender(userRequest.getGender())
                                .address(userRequest.getAddress())
                                .stateOfOrigin(userRequest.getStateOfOrigin())
                                .accountNumber(AccountUtils.generateAccountNumber())
                                .accountBalance(BigDecimal.ZERO)
                                .email(userRequest.getEmail())
                                .phoneNumber(userRequest.getPhoneNumber())
                                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                                .status("ACTIVE")
                                .build();

                User savedUser = userRepo.save(newUser);
                // sending mail alert
                EmailProperties emailProperties = EmailProperties.builder()
                                .recipient(savedUser.getEmail())
                                .messageBody("Congratulation! your account has been successfully created.\n" +
                                                "your account details!\n" +
                                                "Account Name: " + savedUser.getFirstName() + " "
                                                + savedUser.getLastName() + " " + savedUser.getOtherName() + "\n" +
                                                "Account Number: " + savedUser.getAccountNumber())
                                .subject(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                                .build();
                emailService.sendEmailAlert(emailProperties);

                return BankResponse.builder()
                                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                                .accountInfo(AccountUtils.createAccountInfo(savedUser))
                                .build();
        }

        @Override
        public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
                boolean isAccountExists = userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());

                if (!isAccountExists)
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_Message)
                                        .accountInfo(null)
                                        .build();

                User foundUser = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());

                return BankResponse.builder()
                                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                                .responseMessage(AccountUtils.ACCOUNT_FOUND_Message)
                                .accountInfo(AccountUtils.createAccountInfo(foundUser))
                                .build();
        }

        @Override
        public String nameEnquiry(EnquiryRequest enquiryRequest) {
                boolean isAccountExists = userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());

                if (!isAccountExists)
                        return AccountUtils.ACCOUNT_EXISTS_MESSAGE;

                User foundUser = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());

                return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
        }

        @Override
        public BankResponse creditAccount(TransactionRequest transactionRequest) {
                boolean isAccountExists = userRepo.existsByAccountNumber(transactionRequest.getAccountNumber());

                if (!isAccountExists)
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_Message)
                                        .accountInfo(null)
                                        .build();

                User foundUser = userRepo.findByAccountNumber(transactionRequest.getAccountNumber());

                foundUser.setAccountBalance(
                                foundUser.getAccountBalance().add(transactionRequest.getAmount()));
                userRepo.save(foundUser);
                return BankResponse.builder()
                                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE(
                                                transactionRequest.getAccountNumber(), transactionRequest.getAmount(),
                                                foundUser.getAccountBalance()))
                                .accountInfo(AccountUtils.createAccountInfo(foundUser))
                                .build();
        }

        @Override
        public BankResponse debitAccount(TransactionRequest transactionRequest) {
                boolean isAccountExists = userRepo.existsByAccountNumber(transactionRequest.getAccountNumber());

                if (!isAccountExists)
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                                        .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_Message)
                                        .accountInfo(null)
                                        .build();

                User foundUser = userRepo.findByAccountNumber(transactionRequest.getAccountNumber());

                double availableBalance = Double.parseDouble(foundUser.getAccountBalance().toString());
                double debitAmount = Double.parseDouble(transactionRequest.getAmount().toString());

                if (availableBalance < debitAmount)
                        return BankResponse.builder()
                                        .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                                        .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE(
                                                        transactionRequest.getAccountNumber(),
                                                        foundUser.getAccountBalance().toString()))
                                        .accountInfo(AccountUtils.createAccountInfo(foundUser))
                                        .build();

                foundUser.setAccountBalance(foundUser.getAccountBalance().subtract(transactionRequest.getAmount()));
                userRepo.save(foundUser);
                return BankResponse.builder()
                                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE(
                                                transactionRequest.getAccountNumber(), transactionRequest.getAmount(),
                                                foundUser.getAccountBalance()))
                                .accountInfo(AccountUtils.createAccountInfo(foundUser))
                                .build();
        }
}
