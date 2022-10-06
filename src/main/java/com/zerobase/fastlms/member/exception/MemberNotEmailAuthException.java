package com.zerobase.fastlms.member.exception;

import org.springframework.security.core.AuthenticationException;

public class MemberNotEmailAuthException extends AuthenticationException {
    public MemberNotEmailAuthException(String error) {

        super(error);
    }
}
