package com.main.application.utils;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_CREATION_CODE = "002";
    public  static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account!";
    public  static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created!";
    public static String generateAccountNumber() {
        /**
         // 2025 * random six digit
         */
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        /**
         *  generating random number between min and max
         */
        int ranNumber = (int) Math.floor(
                Math.random() * (max - min + 1) + min
        );

        // convert the current and random number to strings, then concatenate them

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(ranNumber);

        StringBuilder accountNumber = new StringBuilder()
                .append(year)
                .append(randomNumber);


        return accountNumber.toString();
    }
}
