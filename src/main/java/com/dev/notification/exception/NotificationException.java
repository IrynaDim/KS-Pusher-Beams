package com.dev.notification.exception;

import org.springframework.http.HttpStatus;

public class NotificationException extends HttpErrorException {
    public NotificationException(String massage) {
        super(HttpStatus.BAD_REQUEST.value(), massage);
    }
}

