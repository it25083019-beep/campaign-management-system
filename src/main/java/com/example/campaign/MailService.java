package com.example.campaign.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWinnerMail(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("【キャンペーン当選のお知らせ】");
        message.setText(
                name + " 様\n\n" +
                        "このたびはキャンペーンにご応募いただき、ありがとうございました。\n" +
                        "抽選の結果、当選されましたのでお知らせいたします。\n\n" +
                        "詳細は別途ご案内いたします。\n\n" +
                        "よろしくお願いいたします。"
        );

        mailSender.send(message);
    }
}