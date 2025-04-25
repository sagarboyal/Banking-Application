package com.main.application.utils;

import java.time.Year;

import com.main.application.payload.response.AccountResponse;
import com.main.application.entity.User;

public class AccountUtils {
        public static AccountResponse createAccountInfo(User user) {
                return AccountResponse.builder()
                                .accountName(user.getFirstName() + " " + user.getLastName()
                                                + " " + user.getOtherName())
                                .accountNumber(user.getAccountNumber())
                                .accountBalance(user.getAccountBalance())
                                .build();
        }

        public static String generateAccountNumber() {
                /**
                 * // 2025 * random six digit
                 */
                Year currentYear = Year.now();
                int min = 100000;
                int max = 999999;

                /**
                 * generating random number between min and max
                 */
                int ranNumber = (int) Math.floor(
                                Math.random() * (max - min + 1) + min);

                String year = String.valueOf(currentYear);
                String randomNumber = String.valueOf(ranNumber);

                StringBuilder accountNumber = new StringBuilder()
                                .append(year)
                                .append(randomNumber);

                return accountNumber.toString();
        }
}
