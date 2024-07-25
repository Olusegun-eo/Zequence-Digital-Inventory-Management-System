package com.zequence.ZequenceIms.service.emailServices;


import com.zequence.ZequenceIms.service.emailServices.exception.MessagingException;

public interface EmailService {
    void sendActivateAccountMessage(String text, String URL) throws MessagingException;
    void sendAccountCreationMessage(String usersEntity, String userName, String confirmationLink)throws MessagingException;

    void sendAccountUpdateMessage(String email, String username, String token) throws jakarta.mail.MessagingException;

    void sendResetPasswordMessage(String email, String resetToken);
}
