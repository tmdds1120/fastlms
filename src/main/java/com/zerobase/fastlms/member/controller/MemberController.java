package com.zerobase.fastlms.member.controller;

import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class MemberController {


    //  생성자 주입을 통해서 객체를 생성

    private final MemberService memberService;

    @RequestMapping("/member/login")
    public String login(){

        return "member/login";
    }


    @GetMapping("/member/find/password")
    public String findPassword(){
        return "/member/find/password";

    }

    //초기화 이메일을 날려주도록 해야함
    @PostMapping("/member/find/password")
    public String findPasswordSubmit(Model model,
            ResetPasswordInput parameter){
        boolean result = false;

        try{
            result = memberService.sendResetPassword(parameter);
        } catch (Exception e){

        }
        model.addAttribute("result", result);
        return "/member/find/find_password_result";
    }

    @GetMapping("/member/register")
    public String register(){
        System.out.println("request get!!");
        return "member/register";
    }


    //request 객체
    //response 객체  server to WEB
    @PostMapping("/member/register")
    public String registerSubmit(Model model, HttpServletRequest req, HttpServletResponse res
        , MemberInput parameter){
        // req,res 뿐만아니라 따로 생성한 데이터 객체의 타입으로 데이터를 받아올 수가 있는데
        // 조건이 뭐지? 1. html 에서 post 작성? postMapping 으로 설정? 객체매개변수로 받기?

        boolean result = memberService.register(parameter);

        System.out.println(result);
        model.addAttribute("result", result);

        System.out.println(parameter.toString());

        /**
         * member 객체 생성후 거기에 post-form 으로 받아온 member 객체에 선언한
         * 멤버변수의 이름과 일치하는 파라미터들을 set, 파라미터.get 을 통해서 값을 설정
         * 그 변수들을 담은 객체의 참조변수 member를 repository의 save를 통해서 리포에 저장
         * request 에서 서버로의 객체 전달 가능한
         * 그 기준을 정해야 한다 , html에서 post 메서드 태그와
         * 그 태그에 해당하는 name 지정이 필요하다
         */


        // 저장 성공 페이지
        return "member/register-complete";
    }

    //http://www.naver.com/news/list.do?id=123

    @GetMapping("/member/email-auth")
    public String emailAuth(Model model,HttpServletRequest req, HttpServletResponse resp){

        String uuid = req.getParameter("id");

        System.out.println(uuid);

        boolean result = memberService.emailAuth(uuid);
        model.addAttribute("result",result);
        return "member/email_auth";

    }

    @GetMapping("/member/info")
    public String memberInfo(){


        return "member/info";
    }


    @GetMapping("/member/reset/password")
    public String resetPassword(Model model,HttpServletRequest req){
        String uuid = req.getParameter("id");

//        model.addAttribute("uuid",uuid);


        // 이 uuid 값이 실제로 패스워드의 값을 초기화하는데 유효한지 검증
        boolean result = memberService.checkResetPassword(uuid);

        model.addAttribute("result",result);


        return "member/reset_password";
    }

    @PostMapping("/member/reset/password")
    public String resetPasswordSubmit(Model model, ResetPasswordInput parameter){
        boolean result =false;
        try{
            result =memberService.resetPassword(parameter.getId(),parameter.getPassword());
        } catch (Exception e){

        }
        model.addAttribute("result",result);

        return "member/reset_password_result";

    }





}
