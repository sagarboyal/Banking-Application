package com.main.application.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmailUtils {

    // Get the current date and time
    private static final LocalDateTime now = LocalDateTime.now();

    // Format the date and time
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String currentTime = now.format(formatter);

    public static String SET_NEW_ACCOUNT_MESSAGE(String customerName, String accountNumber) {
        return String.format(
                "Subject: Welcome to BYTE Bank – Your New Account Details\n\n" +
                        "Dear %s,\n\n" +
                        "We are delighted to welcome you to BYTE Bank! Your new account has been successfully created, and you can now take advantage of our comprehensive banking services.\n\n"
                        +
                        "Here are your account details:\n" +
                        "- Account Number: %s\n" +
                        "- Account Type: Current Account\n" +
                        "- Available Balance: ₹0.00 (Please make an initial deposit to activate your account fully)\n\n"
                        +
                        "To get started, please log in to our online banking portal and make your first deposit. This will ensure your account is fully activated and ready for use.\n\n"
                        +
                        "If you have any questions or need assistance, our customer support team is here to help. Feel free to reach out to us at any time.\n\n"
                        +
                        "Thank you for choosing BYTE Bank. We look forward to serving your financial needs.\n\n" +
                        "Best regards,\n" +
                        "BYTE Bank",
                customerName, accountNumber);
    }

    public static String ACCOUNT_DEBITED_EMAIL_MESSAGE(String name, String destinationAccount, String amountDebited,
            String availableBalance) {

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
                """, name, destinationAccount, amountDebited, currentTime, availableBalance);
    }

    public static String ACCOUNT_CREDITED_EMAIL_MESSAGE(String name, String sourceAccount, String amountCredited,
            String availableBalance) {
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
                """, name, sourceAccount, amountCredited, currentTime, availableBalance);
    }
}
