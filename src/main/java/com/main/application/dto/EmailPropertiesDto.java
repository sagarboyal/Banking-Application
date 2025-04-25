package com.main.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailPropertiesDto {
    private String recipient;
    private String messageBody;
    private String subject;
    private String attachment;
}
