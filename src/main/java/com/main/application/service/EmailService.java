package com.main.application.service;

import com.main.application.dto.EmailPropertiesDto;

public interface EmailService {
    void sendEmailAlert(EmailPropertiesDto properties);
    void sendEmailWithAttachments(EmailPropertiesDto properties);
}
