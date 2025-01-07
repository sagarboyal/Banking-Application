package com.main.application.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmailUtils {

    // Get the current date and time
    private static final LocalDateTime now = LocalDateTime.now();

    // Format the date and time
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String currentTime = now.format(formatter);

    public static String SET_NEW_ACCOUNT_MESSAGE(String customerName, String accountNumber){
        return  String.format(
                "Subject: Your New Account at ABC Bank – Welcome!\n\n" +
                        "Dear %s,\n" +
                        "Welcome to ABC Bank! We are happy to inform you that your account has been successfully opened. You can now enjoy the benefits of banking with us.\n\n" +
                        "Account Details:\n" +
                        "- Account Number: %s\n" +
                        "- Account Type: Current Account\n" +
                        "- Available Balance: ₹0.00 (Please ensure to make an initial deposit to activate your account fully)\n\n" +
                        "Please remember to make your first deposit to activate your account completely.\n\n" +
                        "If you need any help, don't hesitate to contact our support team.\n\n" +
                        "Thank you for choosing ABC Bank. We look forward to serving your banking needs.\n\n" +
                        "Best regards,\n" +
                        "ABC Bank",
                customerName, accountNumber);
    }

    public static String ACCOUNT_DEBITED_EMAIL_MESSAGE(String name, String destinationAccount,String amountDebited, String availableBalance){

        return String.format("""
    Dear Customer,
    %s
    
    We wish to inform you that a debit transaction has been made from your account.

    Transaction Details:
    - Account Number: %s
    - Amount Debited: %s
    - Transaction Date & Time: %s
    - Available Balance: %s

    If you did not authorize this transaction, please contact our customer support immediately.

    Thank you for banking with us.

    Warm regards,
    BYTE Bank
    """,name, destinationAccount, amountDebited, currentTime, availableBalance);
    }

    public static String ACCOUNT_CREDITED_EMAIL_MESSAGE(String name, String sourceAccount,String amountCredited, String availableBalance){
        return String.format("""
            Dear Customer,
            %s
            
            We wish to inform you that a credit transaction has been made to your account.

            Transaction Details:
            - Account Number: %s
            - Amount Credited: %s
            - Transaction Date & Time: %s
            - Available Balance: %s

            If you have any queries regarding this transaction, please contact our customer support.

            Thank you for banking with us.

            Warm regards,
            BYTE Bank
            """,name, sourceAccount, amountCredited, currentTime, availableBalance);
    }
}
