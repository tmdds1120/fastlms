package com.zerobase.fastlms.main.controller;

import com.zerobase.fastlms.admin.banner.dto.BannerDto;
import com.zerobase.fastlms.admin.banner.service.BannerService;
import com.zerobase.fastlms.common.components.MailComponents;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BannerService bannerService;
    private final MailComponents mailComponents;
    @RequestMapping("/")
    public String index(Model model){
        List<BannerDto> banners= bannerService.frontList();
        model.addAttribute("banners", banners);

       return "index";
    }

    @RequestMapping("/error/denied")
    public String errorDenied(){
        return "error/denied";
    }
}
