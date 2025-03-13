package br.edu.infnet.pageflow.service;

import br.edu.infnet.pageflow.entities.BlogUser;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final MessageSource messages;

    public EmailService(JavaMailSender mailSender, MessageSource messages) {
        this.mailSender = mailSender;
        this.messages = messages;
    }

    public SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, BlogUser user) {
        String url = contextPath + "/changePassword?token=" + token;
        String message = messages.getMessage("message.resetPasswordEmail", null, locale);
        return constructEmail("Reset password", message + "\r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, BlogUser user) {

        SimpleMailMessage email = new SimpleMailMessage();

        email.setFrom("noreply@pageflow.com");
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText(body);

        return email;

    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("noreply@pageflow.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
