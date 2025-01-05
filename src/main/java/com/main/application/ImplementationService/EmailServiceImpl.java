package com.main.application.ImplementationService;

import com.main.application.dto.EmailProperties;
import com.main.application.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendEmailAlert(EmailProperties properties) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(properties.getRecipient());
            mailMessage.setText(properties.getMessageBody());
            mailMessage.setSubject(properties.getSubject());

            javaMailSender.send(mailMessage);
            System.out.println("Mail sent successfully");
        }catch (MailException e){
            throw new RuntimeException(e);
        }
    }
}
