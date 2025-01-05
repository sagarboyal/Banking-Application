package com.main.application.service;

import com.main.application.dto.EmailProperties;

public interface EmailService {
    void sendEmailAlert(EmailProperties properties);
}
