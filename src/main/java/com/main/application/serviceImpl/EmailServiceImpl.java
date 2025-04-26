package com.main.application.serviceImpl;

import com.main.application.dto.EmailPropertiesDto;
import com.main.application.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendEmailAlert(EmailPropertiesDto properties) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(properties.getRecipient());
            mailMessage.setText(properties.getMessageBody());
            mailMessage.setSubject(properties.getSubject());

            javaMailSender.send(mailMessage);
            System.out.println("Mail sent successfully");
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEmailWithAttachments(EmailPropertiesDto properties) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(senderEmail);
            mimeMessageHelper.setTo(properties.getRecipient());
            mimeMessageHelper.setText(properties.getMessageBody());
            mimeMessageHelper.setSubject(properties.getSubject());

            FileSystemResource systemResource = new FileSystemResource(new File(properties.getAttachment()));
            mimeMessageHelper.addAttachment(systemResource.getFilename(), systemResource);
            javaMailSender.send(mimeMessage);

            log.info("{} has been sent to user with email {}", systemResource.getFilename(), properties.getRecipient());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
