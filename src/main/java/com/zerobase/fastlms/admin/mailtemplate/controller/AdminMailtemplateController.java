package com.zerobase.fastlms.admin.mailtemplate.controller;

import com.zerobase.fastlms.admin.mailtemplate.dto.MailtemplateDto;
import com.zerobase.fastlms.admin.mailtemplate.model.MailtemplateInput;
import com.zerobase.fastlms.admin.mailtemplate.model.MailtemplateParam;
import com.zerobase.fastlms.admin.mailtemplate.service.MailtemplateService;
import com.zerobase.fastlms.course.controller.BaseController;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminMailtemplateController extends BaseController {
    
    private final MemberService memberService;
    private final MailtemplateService mailtemplateService;
    
    @GetMapping("/admin/mailtemplate/list.do")
    public String list(Model model, MailtemplateParam parameter) {
        
        parameter.init();
        List<MailtemplateDto> members = mailtemplateService.list(parameter);
        
        long totalCount = 0;
        if (members != null && members.size() > 0) {
            totalCount = members.get(0).getTotalCount();
        }
        String queryString = parameter.getQueryString();
        String pagerHtml = getPagerHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(), queryString);
        
        model.addAttribute("list", members);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);
        
        
        return "admin/mailtemplate/list";
    }
    
    @GetMapping(value = {"/admin/mailtemplate/add.do", "/admin/mailtemplate/edit.do"})
    public String add(Model model, HttpServletRequest request
            , MailtemplateInput parameter) {
    
        boolean editMode = request.getRequestURI().contains("/edit.do");
        MailtemplateDto detail = new MailtemplateDto();
    
        if (editMode) {
            long id = parameter.getId();
            MailtemplateDto mailtemplate = mailtemplateService.getById(id);
            if (mailtemplate == null) {
                // error 처리
                model.addAttribute("message", "강좌정보가 존재하지 않습니다.");
                return "common/error";
            }
            detail = mailtemplate;
        }
    
        model.addAttribute("editMode", editMode);
        model.addAttribute("detail", detail);
    
        return "admin/mailtemplate/add";
    }
    
    @PostMapping(value = {"/admin/mailtemplate/add.do", "/admin/mailtemplate/edit.do"})
    public String addSubmit(Model model, HttpServletRequest request
            , MailtemplateInput parameter) {
        
        boolean editMode = request.getRequestURI().contains("/edit.do");
        
        if (editMode) {
            long id = parameter.getId();
            MailtemplateDto existMailtemplate = mailtemplateService.getById(id);
            if (existMailtemplate == null) {
                model.addAttribute("message", "강좌정보가 존재하지 않습니다.");
                return "common/error";
            }
            boolean result = mailtemplateService.set(parameter);
            
        } else {
            boolean result = mailtemplateService.add(parameter);
        }
        
        return "redirect:/admin/mailtemplate/list.do";
    }
    
    @PostMapping("/admin/mailtemplate/delete.do")
    public String del(Model model, HttpServletRequest request
            , CourseInput parameter) {
        
        return "redirect:/admin/mailtemplate/list.do";
    }
    
    
}
