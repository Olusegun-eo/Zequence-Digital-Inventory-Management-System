package com.zequence.ZequenceIms.service.emailServices.exception;

import org.springframework.mail.MailException;

public class MessagingException extends Exception{
    public MessagingException() {
    }
    public MessagingException(String message) {
        super(message);
    }

    public MessagingException(String s, MailException e) {
    }
}
