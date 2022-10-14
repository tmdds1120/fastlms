package com.zerobase.fastlms.main.controller;

import com.zerobase.fastlms.common.components.MailComponents;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainController {


    private final MailComponents mailComponents;
    @RequestMapping("/")
    public String index(){
        String email = "tmdds1120@naver.com";
        String subject = "안녕하세요 황승윤입니다";
        // 태그가 적용되서 메일을통해 링크나 html 적용이 된다?
        String text= "<p>안녕하세요.</p><p>반갑습니다 .</p>";
        mailComponents.sendMail(email,subject,text);
        return "index";
    }

    @RequestMapping("/error/denied")
    public String errorDenied(){
        return "error/denied";
    }
}
