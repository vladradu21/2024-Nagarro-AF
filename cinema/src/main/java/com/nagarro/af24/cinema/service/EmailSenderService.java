package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.dto.EmailDTO;
import com.nagarro.af24.cinema.exception.CustomConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    private final String mailUsername;
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(@Value("${spring.mail.username}") String mailUsername, JavaMailSender javaMailSender) {
        this.mailUsername = mailUsername;
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(EmailDTO emailDTO) {
        try {
            SimpleMailMessage message = formatMessage(
                    emailDTO.email(),
                    emailDTO.subject(),
                    emailDTO.body()
            );
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new CustomConflictException("Email could not be sent");
        }
    }

    private SimpleMailMessage formatMessage(String email, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }
}