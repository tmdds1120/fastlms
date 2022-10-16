package com.zerobase.fastlms.member.controller;

import com.zerobase.fastlms.member.dto.MemberDto;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.service.TakeCourseService;
import com.zerobase.fastlms.member.model.AdminMemberInput;
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
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {


    //  생성자 주입을 통해서 객체를 생성

    private final MemberService memberService;
    private final TakeCourseService takeCourseService;

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


        boolean result = memberService.register(parameter);

        System.out.println(result);
        model.addAttribute("result", result);



        /**
         * member 객체 생성후 거기에 post-form 으로 받아온 member 객체에 선언한
         * 멤버변수의 이름과 일치하는 파라미터들을 set, 파라미터.get 을 통해서 값을 설정
         * 그 변수들을 담은 객체의 참조변수 member를 repository의 save를 통해서 리포에 저장
         * request 에서 서버로의  전달 가능한
         * 그 기준을 정해야 한다
         *
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

    @GetMapping("/member/info")
    public String memberInfo(Model model, Principal principal){

        String userId =principal.getName();
        MemberDto detail = memberService.detail(userId);

        model.addAttribute("detail",detail);
        return "member/info";
    }
    @PostMapping("/member/info")
    public String memberInfoSubmit(
            Model model, MemberInput parameter, Principal principal){
        String userId = principal.getName();
        parameter.setUserId(userId);

        ServiceResult result = memberService.updateMember(parameter);
        if (!result.isResult()){
            model.addAttribute("message",result.getMessage());
            return "common/error";
        }

        return "redirect:/member/info";
    }


    @GetMapping("/member/password")
    public String memberPassword(Model model, Principal principal){
        String userId = principal.getName();
        MemberDto detail = memberService.detail(userId);

        model.addAttribute("detail", detail);

        return "member/password";
    }

    @PostMapping("/member/password")
    public String memberPasswordSubmit(Model model
    , MemberInput parameter
    , Principal principal){

        String userId = principal.getName();
        parameter.setUserId(userId);

        ServiceResult result = memberService.updateMemberPassword(parameter);
        if (!result.isResult()){
            model.addAttribute("message",result.getMessage());
            return "common/error";
        }

        return "redirect:/member/info";
    }

    @GetMapping("/member/takecourse")
    public String memberTakeCourse(Model model,Principal principal){
        String userId = principal.getName();
        List<TakeCourseDto> list = takeCourseService.myCourse(userId);

        model.addAttribute("list",list);

        return "member/takecourse";
    }

    @GetMapping("/member/withdraw")
    public String memberWithdraw(Model model){
        return "member/withdraw";
    }


    @PostMapping("/member/withdraw")
    public String memberWithdrawSubmit(Model model
    , AdminMemberInput parameter
    ,Principal principal){
        String userId = principal.getName();


        ServiceResult result = memberService.withdraw(userId,parameter.getPassword());
        if(!result.isResult()){
            model.addAttribute("message",result.getMessage());

            return "common/error";
        }

        return "redirect:/member/logout";
    }





}
