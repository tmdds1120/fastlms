package com.zerobase.fastlms.member.components;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 *  기능  - 인증 메일 전송하기
 *  요구사항? - 메일 서버 세팅 필요 , smtp 프로토콜로 보내는데
 *  현재는 불가함 메일서버
 *  ==> 구글에서 메일전송 기능 사용
 */
@Component
@RequiredArgsConstructor
public class MailComponents {
    private final JavaMailSender javaMailSender;
    public void sendMailTest(){

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("tmdds1120@naver.com");
        msg.setSubject("이메일 테스트입니다 아이야");
        msg.setText("안녕하시오 공부중이다아이아");
        javaMailSender.send(msg);
    }

    public boolean sendMail(String mail, String Subject, String text){
        boolean result = false;
        MimeMessagePreparator msg = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper
                        = new MimeMessageHelper(mimeMessage, true,"UTF-8");

                mimeMessageHelper.setTo(mail);
                mimeMessageHelper.setSubject(Subject);
                //
                mimeMessageHelper.setText(text, true);
            }
        };
        try {
            javaMailSender.send(msg);
            result = true;
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        // 구체적으로 보내기 위한 send 의 MimeMessageParpartor 선택


        return result;
    }




}
