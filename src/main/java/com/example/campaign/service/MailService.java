package com.example.campaign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendWinnerMail(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("TEST MAIL SPRING BOOT");
        message.setText(
                name + " 様\n\n" +
                        "これはテストメールです。\n" +
                        "キャンペーン当選のお知らせです。"
        );

        mailSender.send(message);
    }
}