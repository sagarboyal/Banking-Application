package com.main.application.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EnquiryRequest {
    private String accountNumber;
}
