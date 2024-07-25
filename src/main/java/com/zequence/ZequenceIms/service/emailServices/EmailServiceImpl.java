package com.zequence.ZequenceIms.service.emailServices;


import com.zequence.ZequenceIms.service.emailServices.exception.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendActivateAccountMessage(String email, String code) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ZEQUENCE");
        message.setTo(email);
        message.setSubject("Account Activation");

        String template = "Dear User,\n"
                + "Thanks for registering on ZEQUENCE.\n"
                + "Kindly use the code below to validate your account and activate your account:\n"
                + code + "\n"
                + "Thank you.\n"
                + "ZEQUENCE team";

        message.setText(template);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new MessagingException("Failed to send account activation message", e);
        }
    }

    @Override
    public void sendAccountCreationMessage(String email, String userName, String confirmationLink) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ZEQUENCE");
        message.setTo(email);
        message.setSubject("Welcome to ZEQUENCE");

        String template = "Dear [[name]],\n"
                + "Your account has been verified, kindly login to explore:\n"
                + "[[URL]]\n"
                + "Thank you,\n\n"
                + "The Zequence Team";

        // Replace placeholders with actual values
        template = template.replace("[[name]]", (userName != null) ? userName : "User");
        template = template.replace("[[URL]]", (confirmationLink != null) ? confirmationLink : "");

        message.setText(template);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            throw new MessagingException("Failed to send account creation message", e);
        }
    }

    public void sendAccountUpdateMessage(String email, String username, String token) throws jakarta.mail.MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String verificationLink = "http://localhost:8080/api/auth/activate?token=" + token;
        String htmlMsg = "<p>Hello " + username + ",</p>" +
                "<p>Your account details have been successfully updated. Please activate your account using the link below.</p>" +
                "<a href=\"" + verificationLink + "\">Activate your account</a>";
        helper.setText(htmlMsg, true);
        helper.setTo(email);
        helper.setSubject("Account Update Confirmation");
        helper.setFrom("your-email@example.com");
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendResetPasswordMessage(String email, String resetToken) {

    }
}
//password: gmvfoosxezapzooi


// run===>maidev
//type link==>http://127.0.0.1:1080   or http://localhost:1080