package com.project.DATN2026.service;

import com.project.DATN2026.entity.Account;
import com.project.DATN2026.entity.VerificationCode;

import javax.mail.MessagingException;

public interface VerificationCodeService {
    VerificationCode createVerificationCode(String email) throws MessagingException;

    Account verifyCode( String code);
}