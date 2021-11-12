package com.arjolpanci.restservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService{

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage newMessage = new SimpleMailMessage();
        newMessage.setFrom("apanci18@epoka.edu.al");
        newMessage.setTo(toEmail);
        newMessage.setSubject(subject);
        newMessage.setText(body);

        mailSender.send(newMessage);

    }

}
