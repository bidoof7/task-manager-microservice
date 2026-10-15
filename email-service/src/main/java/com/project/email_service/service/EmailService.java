package com.project.email_service.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendAccountCreatedEmail(String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true,
                    "UTF-8"
            );

            helper.setTo(email);
            helper.setFrom(new InternetAddress("duy9803@gmail.com"));
            helper.setSubject("Welcome !");

            String html = """
                    <!DOCTYPE html> <html> <head> <meta charset="UTF-8"> </head> <body style=" margin: 0; padding: 0; background-color: #f4f4f5; font-family: Arial, sans-serif; "> <div style=" max-width: 600px; margin: 40px auto; background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08); "> <div style=" padding: 24px; background-color: #18181b; color: #ffffff; text-align: center; "> <h1 style=" margin: 0; font-size: 24px; "> Account Created </h1> </div> <div style="padding: 32px;"> <p style=" margin-top: 0; font-size: 16px; color: #27272a; "> Dear <strong>%s</strong>, </p> <p style=" font-size: 15px; line-height: 1.6; color: #52525b; "> Your account has been successfully created. Welcome to our service! </p> <div style=" margin: 24px 0; padding: 16px; background-color: #f4f4f5; border-radius: 8px; "> <p style=" margin: 0; font-size: 14px; color: #52525b; "> You can now log in and start using your account. </p> </div> <p style=" margin-bottom: 0; font-size: 14px; color: #71717a; "> Have a great day! </p> </div> <div style=" padding: 16px 24px; border-top: 1px solid #e4e4e7; text-align: center; color: #a1a1aa; font-size: 12px; "> This is an automated email. Please do not reply. </div> </div> </body> </html>
                    """.formatted(email);

            helper.setText(html, true);
            mailSender.send(message);
        } catch (MailException ex) {
            log.error("Mail send error with: email={}",
                    email,
                    ex
            );
        } catch (MessagingException ex) {
            log.error("Fail to create email: email={}",
                    email,
                    ex
            );
        }
    }
}
