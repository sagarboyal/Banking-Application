package com.main.application.ImplementationService;

import com.main.application.dto.AccountInfo;
import com.main.application.dto.BankResponse;
import com.main.application.dto.EmailProperties;
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
                                .accountInfo(AccountInfo.builder()
                                                .accountName(savedUser.getFirstName() + " " + savedUser.getLastName()
                                                                + " " + savedUser.getOtherName())
                                                .accountNumber(savedUser.getAccountNumber())
                                                .accountBalance(savedUser.getAccountBalance())
                                                .build())
                                .build();
        }
}
