package com.project.DATN2026.service;

import com.project.DATN2026.dto.MailInfo;

import javax.mail.MessagingException;

public interface MailerService {
    void send(MailInfo mail) throws MessagingException;

    void queue(MailInfo mail);
}
