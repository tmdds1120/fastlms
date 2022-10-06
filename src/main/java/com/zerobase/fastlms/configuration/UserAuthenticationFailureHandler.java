package com.zerobase.fastlms.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    // 에러가 발생됫을때 실행되는 메서드
    // 그메서드의 오버라이딩을 통해서 에러 발생시 에러 메세지를 반환 할것임


    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException exception) throws IOException, ServletException {


        String msg = "로그인에 실패하였습니다.";
        if (exception instanceof InternalAuthenticationServiceException){
            msg = exception.getMessage();
        }


        setUseForward(true);
        setDefaultFailureUrl("/member/login?error=true");
        req.setAttribute("errorMessage",msg);

        System.out.println("로그인에 실패하였습니다");

        super.onAuthenticationFailure(req,resp,exception);

    }
}
