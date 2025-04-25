package com.main.application.ImplementationService;

import com.main.application.constraint.AccountConstraint;
import com.main.application.dto.*;
import com.main.application.entity.Role;
import com.main.application.entity.User;
import com.main.application.exception.ResourceFoundException;
import com.main.application.exception.ResourceNotFoundException;
import com.main.application.payload.request.EnquiryRequest;
import com.main.application.payload.request.TransactionRequest;
import com.main.application.payload.request.TransferRequest;
import com.main.application.payload.request.UserRequest;
import com.main.application.payload.response.BankResponse;
import com.main.application.repository.UserRepo;
import com.main.application.jwt.JwtTokenProvider;
import com.main.application.service.EmailService;
import com.main.application.service.TransactionService;
import com.main.application.service.UserService;
import com.main.application.utils.AccountUtils;
import com.main.application.utils.EmailUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

        private final UserRepo userRepo;
        private final EmailService emailService;
        private final TransactionService transactionService;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;
        private final JwtTokenProvider jwtTokenProvider;

        @Override
        public BankResponse createAccount(UserRequest userRequest) {

                if (userRepo.existsByEmail(userRequest.getEmail()))
                        throw new ResourceFoundException(userRequest.getEmail(), "email");

                if (userRequest.getRole() != null && !EnumSet.allOf(Role.class).contains(userRequest.getRole())) {
                        throw new RuntimeException("Invalid role provided");
                }

                Role role = userRequest.getRole() != null ?
                        Role.valueOf(userRequest.getRole().toString().toUpperCase()) : Role.USER;

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
                                .password(passwordEncoder.encode(userRequest.getPassword()))
                                .role(role)
                                .phoneNumber(userRequest.getPhoneNumber())
                                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                                .status("ACTIVE")
                                .build();

                User savedUser = userRepo.save(newUser);
                // sending mail alert
                EmailPropertiesDto emailPropertiesDto = EmailPropertiesDto.builder()
                                .recipient(savedUser.getEmail())
                                .messageBody(EmailUtils.SET_NEW_ACCOUNT_MESSAGE(
                                        savedUser.getFirstName()+" "+savedUser.getLastName()
                                ,savedUser.getAccountNumber()))
                                .subject(AccountConstraint.ACCOUNT_CREATION_MESSAGE)
                                .build();
                emailService.sendEmailAlert(emailPropertiesDto);

                return BankResponse.builder()
                                .responseCode(AccountConstraint.ACCOUNT_CREATION_CODE)
                                .responseMessage(AccountConstraint.ACCOUNT_CREATION_MESSAGE)
                                .accountResponse(AccountUtils.createAccountInfo(savedUser))
                                .build();
        }

        @Override
        public BankResponse updateAccount(UserRequest userRequest) {
                User savedUser = userRepo.findByEmail(userRequest.getEmail()).
                        orElseThrow(() -> new ResourceNotFoundException(userRequest.getEmail(), "email"));

                User updatedInfo = User.builder()
                        .firstName(userRequest.getFirstName() != null ? userRequest.getFirstName() : savedUser.getFirstName())
                        .lastName(userRequest.getLastName() != null ? userRequest.getLastName() : savedUser.getLastName())
                        .otherName(userRequest.getOtherName() != null ? userRequest.getOtherName() : savedUser.getOtherName())
                        .gender(userRequest.getGender() != null ? userRequest.getGender() : savedUser.getGender())
                        .address(userRequest.getAddress() != null ? userRequest.getAddress() : savedUser.getAddress())
                        .stateOfOrigin(userRequest.getStateOfOrigin() != null ? userRequest.getStateOfOrigin() : savedUser.getStateOfOrigin())
                        .accountNumber(savedUser.getAccountNumber()) // Assuming account number doesn't change
                        .accountBalance(savedUser.getAccountBalance()) // Assuming account balance doesn't change
                        .email(savedUser.getEmail()) // Email should not change
                        .password(userRequest.getPassword() != null ? passwordEncoder.encode(userRequest.getPassword()) : savedUser.getPassword())
                        .role(userRequest.getRole() != null ? Role.valueOf(userRequest.getRole().toString().toUpperCase()) : savedUser.getRole())
                        .phoneNumber(userRequest.getPhoneNumber() != null ? userRequest.getPhoneNumber() : savedUser.getPhoneNumber())
                        .alternativePhoneNumber(userRequest.getAlternativePhoneNumber() != null ? userRequest.getAlternativePhoneNumber() : savedUser.getAlternativePhoneNumber())
                        .status(savedUser.getStatus()) // Assuming status doesn't change
                        .build();
                User savedData = userRepo.save(updatedInfo);

                return BankResponse.builder()
                        .responseCode(AccountConstraint.ACCOUNT_UPDATED_CODE)
                        .responseMessage(AccountConstraint.ACCOUNT_UPDATED_MESSAGE)
                        .accountResponse(AccountUtils.createAccountInfo(savedData))
                        .build();
        }

        @Override
        public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
                boolean isAccountExists = userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());

                if (!isAccountExists)
                        throw new ResourceNotFoundException(enquiryRequest.getAccountNumber(), "accountNumber");

                User foundUser = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());

                return BankResponse.builder()
                                .responseCode(AccountConstraint.ACCOUNT_FOUND_CODE)
                                .responseMessage(AccountConstraint.ACCOUNT_FOUND_Message)
                                .accountResponse(AccountUtils.createAccountInfo(foundUser))
                                .build();
        }

        @Override
        public String nameEnquiry(EnquiryRequest enquiryRequest) {
                boolean isAccountExists = userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());

                if (!isAccountExists)
                        throw new ResourceNotFoundException(enquiryRequest.getAccountNumber(), "accountNumber");

                User foundUser = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());

                return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
        }

        @Override
        public BankResponse creditAccount(TransactionRequest transactionRequest) {
                boolean isAccountExists = userRepo.existsByAccountNumber(transactionRequest.getAccountNumber());

                if (!isAccountExists)
                        return BankResponse.builder()
                                        .responseCode(AccountConstraint.ACCOUNT_NOT_EXISTS_CODE)
                                        .responseMessage(AccountConstraint.ACCOUNT_NOT_EXISTS_Message)
                                        .accountResponse(null)
                                        .build();

                User foundUser = userRepo.findByAccountNumber(transactionRequest.getAccountNumber());

                foundUser.setAccountBalance(
                                foundUser.getAccountBalance().add(transactionRequest.getAmount()));
                TransactionDto creditTransactionRecord = TransactionDto.builder()
                        .transactionType("CREDIT")
                        .accountNumber(transactionRequest.getAccountNumber())
                        .amount(transactionRequest.getAmount())
                        .build();

                userRepo.save(foundUser);
                transactionService.saveTransaction(creditTransactionRecord);

                EmailPropertiesDto creditAlert = EmailPropertiesDto.builder()
                        .subject("CREDIT ALERT")
                        .recipient(foundUser.getEmail())
                        .messageBody(EmailUtils.ACCOUNT_CREDITED_EMAIL_MESSAGE(foundUser.getFirstName()+" "+foundUser.getLastName()
                                ,transactionRequest.getAccountNumber(),
                                transactionRequest.getAmount().toString(),
                                foundUser.getAccountBalance().toString()))
                        .build();
                emailService.sendEmailAlert(creditAlert);

                return BankResponse.builder()
                                .responseCode(AccountConstraint.ACCOUNT_CREDITED_SUCCESS_CODE)
                                .responseMessage(AccountConstraint.ACCOUNT_CREDITED_SUCCESS_MESSAGE(
                                                transactionRequest.getAccountNumber(), transactionRequest.getAmount(),
                                                foundUser.getAccountBalance()))
                                .accountResponse(AccountUtils.createAccountInfo(foundUser))
                                .build();
        }

        @Override
        public BankResponse debitAccount(TransactionRequest transactionRequest) {
                boolean isAccountExists = userRepo.existsByAccountNumber(transactionRequest.getAccountNumber());

                if (!isAccountExists)
                        throw new ResourceNotFoundException(transactionRequest.getAccountNumber(), "accountNumber");

                User foundUser = userRepo.findByAccountNumber(transactionRequest.getAccountNumber());

                double availableBalance = Double.parseDouble(foundUser.getAccountBalance().toString());
                double debitAmount = Double.parseDouble(transactionRequest.getAmount().toString());

                if (availableBalance < debitAmount)
                        return BankResponse.builder()
                                        .responseCode(AccountConstraint.INSUFFICIENT_BALANCE_CODE)
                                        .responseMessage(AccountConstraint.INSUFFICIENT_BALANCE_MESSAGE(
                                                        transactionRequest.getAccountNumber(),
                                                        foundUser.getAccountBalance().toString()))
                                        .accountResponse(AccountUtils.createAccountInfo(foundUser))
                                        .build();

                foundUser.setAccountBalance(foundUser.getAccountBalance().subtract(transactionRequest.getAmount()));
                TransactionDto debitTransactionRecord = TransactionDto.builder()
                        .transactionType("DEBIT")
                        .accountNumber(transactionRequest.getAccountNumber())
                        .amount(transactionRequest.getAmount())
                        .build();

                userRepo.save(foundUser);
                transactionService.saveTransaction(debitTransactionRecord);

                EmailPropertiesDto debitAlert = EmailPropertiesDto.builder()
                        .subject("DEBIT ALERT")
                        .recipient(foundUser.getEmail())
                        .messageBody(EmailUtils.ACCOUNT_DEBITED_EMAIL_MESSAGE(foundUser.getFirstName()+" "+foundUser.getLastName()
                                ,transactionRequest.getAccountNumber(),
                                transactionRequest.getAmount().toString(),
                                foundUser.getAccountBalance().toString()))
                        .build();
                emailService.sendEmailAlert(debitAlert);

                return BankResponse.builder()
                                .responseCode(AccountConstraint.ACCOUNT_DEBITED_SUCCESS_CODE)
                                .responseMessage(AccountConstraint.ACCOUNT_DEBITED_SUCCESS_MESSAGE(
                                                transactionRequest.getAccountNumber(), transactionRequest.getAmount(),
                                                foundUser.getAccountBalance()))
                                .accountResponse(AccountUtils.createAccountInfo(foundUser))
                                .build();
        }

        @Override
        public BankResponse transferAmount(TransferRequest transferRequest) {

                boolean isDestinationAccountExists = userRepo.existsByAccountNumber(transferRequest.getDestinationAccountNumber());

                if(!isDestinationAccountExists)
                        return BankResponse.builder()
                                .responseCode(AccountConstraint.ACCOUNT_NOT_EXISTS_CODE)
                                .responseMessage(AccountConstraint.ACCOUNT_DEBITED_NOT_EXISTS_MESSAGE(transferRequest.getDestinationAccountNumber()))
                                .accountResponse(null)
                                .build();


                User sourceAccount = userRepo.findByAccountNumber(transferRequest.getSourceAccountNumber());
                User destinationAccount = userRepo.findByAccountNumber(transferRequest.getDestinationAccountNumber());

                /**
                 *  that mean transfer amount is large
                 *  source account doesn't have that amount of money.
                 */

                if(transferRequest.getAmount().compareTo(sourceAccount.getAccountBalance()) > 0)
                        return BankResponse.builder()
                                .responseCode(AccountConstraint.INSUFFICIENT_BALANCE_CODE)
                                .responseMessage(AccountConstraint.INSUFFICIENT_BALANCE_MESSAGE(transferRequest.getSourceAccountNumber(), sourceAccount.getAccountBalance().toString()))
                                .accountResponse(AccountUtils.createAccountInfo(sourceAccount))
                                .build();

                sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(transferRequest.getAmount()));
                TransactionDto debitTransactionRecord = TransactionDto.builder()
                        .transactionType("DEBIT")
                        .accountNumber(sourceAccount.getAccountNumber())
                        .amount(transferRequest.getAmount())
                        .build();
                userRepo.save(sourceAccount);
                transactionService.saveTransaction(debitTransactionRecord);

                EmailPropertiesDto debitAlert = EmailPropertiesDto.builder()
                        .subject("DEBIT ALERT")
                        .recipient(sourceAccount.getEmail())
                        .messageBody(EmailUtils.ACCOUNT_DEBITED_EMAIL_MESSAGE(sourceAccount.getFirstName()+" "+sourceAccount.getLastName(),
                                transferRequest.getDestinationAccountNumber(),
                                transferRequest.getAmount().toString(),
                                sourceAccount.getAccountBalance().toString()))
                        .build();
                emailService.sendEmailAlert(debitAlert);

                destinationAccount.setAccountBalance(destinationAccount.getAccountBalance().add(transferRequest.getAmount()));
                TransactionDto creditTransactionRecord = TransactionDto.builder()
                        .transactionType("CREDIT")
                        .accountNumber(destinationAccount.getAccountNumber())
                        .amount(transferRequest.getAmount())
                        .build();

                userRepo.save(destinationAccount);
                transactionService.saveTransaction(creditTransactionRecord);
                EmailPropertiesDto creditAlert = EmailPropertiesDto.builder()
                        .subject("CREDIT ALERT")
                        .recipient(destinationAccount.getEmail())
                        .messageBody(EmailUtils.ACCOUNT_CREDITED_EMAIL_MESSAGE(destinationAccount.getFirstName()+" "+destinationAccount.getLastName()
                                ,transferRequest.getSourceAccountNumber(),
                                transferRequest.getAmount().toString(),
                                destinationAccount.getAccountBalance().toString()))
                        .build();
                emailService.sendEmailAlert(creditAlert);

                return BankResponse.builder()
                        .responseCode(AccountConstraint.TRANSACTION_SUCCESS_CODE)
                        .responseMessage(AccountConstraint.TRANSACTION_SUCCESS_MESSAGE("",
                                sourceAccount.getAccountNumber(),
                                destinationAccount.getAccountNumber(),
                                transferRequest.getAmount().toString()))
                        .accountResponse(null)
                        .build();
        }

        @Override
        public BankResponse login(LoginDto loginDto) {

                Authentication authentication = authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                          loginDto.getPassword())
                );
                EmailPropertiesDto loginAlert = EmailPropertiesDto.builder()
                        .subject("You're logged in!")
                        .recipient(loginDto.getEmail())
                        .messageBody("You logged into your account!")
                        .build();
                //emailService.sendEmailAlert(loginAlert);
                return BankResponse.builder()
                        .responseCode(AccountConstraint.ACCOUNT_LOGIN_CODE)
                        .responseMessage(jwtTokenProvider.generateToken(authentication))
                        .build();
        }

}
