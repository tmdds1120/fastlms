package com.zerobase.fastlms.member.exception;

import org.springframework.security.core.AuthenticationException;

public class MemberStopUserException extends AuthenticationException {
    public MemberStopUserException(String error) {

        super(error);
    }
}
