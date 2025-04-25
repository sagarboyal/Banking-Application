package com.main.application.constraint;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountConstraint {
    private static final LocalDateTime now = LocalDateTime.now();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String currentTime = now.format(formatter);

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account!";

    public static final String ACCOUNT_CREATION_CODE = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created!";

    public static final String ACCOUNT_UPDATED_CODE = "010";
    public static final String ACCOUNT_UPDATED_MESSAGE = "Account has been updated successfully!";

    public static final String ACCOUNT_NOT_EXISTS_CODE = "003";
    public static final String ACCOUNT_NOT_EXISTS_Message = "User with the provide account number not exists!";

    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_Message = "User account found!";

    public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "005";

    public static String ACCOUNT_CREDITED_SUCCESS_MESSAGE(String accountNumber, BigDecimal amount,
                                                          BigDecimal totalAmount) {
        return "Your A/C XXXXX" + accountNumber.substring(6) + " Credited INR " + amount + " on " + currentTime
                + ". Available Balance INR " + totalAmount;
    }

    public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "006";
    public static String ACCOUNT_DEBITED_SUCCESS_MESSAGE(String accountNumber, BigDecimal amount,
                                                         BigDecimal totalAmount) {
        return "Your A/C XXXXX" + accountNumber.substring(6) + " Debited INR " + amount + " on " + currentTime
                + ". Available Balance INR " + totalAmount;
    }

    public static String ACCOUNT_DEBITED_NOT_EXISTS_MESSAGE(String accountNumber) {
        return "Your A/C " + accountNumber + " you're trying to reach is not exists!";
    }

    public static final String INSUFFICIENT_BALANCE_CODE = "007";
    public static String INSUFFICIENT_BALANCE_MESSAGE(String accountNumber, String balance) {
        return "Transaction failed! Your A/C XXXXX" + accountNumber.substring(6) + " has returned on "
                + currentTime
                + " due to Insufficient fund. Applicable return charges will be levied. Available Balance INR "
                + balance;
    }

    public static final String TRANSACTION_SUCCESS_CODE = "008";
    public static String TRANSACTION_SUCCESS_MESSAGE(String transactionId, String senderAccount,
                                                     String recipientAccount,
                                                     String amountTransferred) {
        return String.format(
                "Transfer Success! | Transaction ID: %s | Sender Account: %s | Recipient Account: %s | Amount Transferred: %s | Transaction Date & Time: %s | Thank you for using our services.",
                transactionId, senderAccount, recipientAccount, amountTransferred, currentTime);

    }

    public static final String ACCOUNT_LOGIN_CODE = "201";
    public static final String ACCOUNT_LOGIN_MESSAGE = "Account has been successfully logged in!";
}
