package com.zerobase.fastlms.admin.controller;


import com.zerobase.fastlms.member.entity.Member;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class AdminMainController {




    @GetMapping("/admin/main.do")
    public String main(){


        return "admin/main";
    }


}
