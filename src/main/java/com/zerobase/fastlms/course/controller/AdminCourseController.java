package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.service.CourseService;
import com.zerobase.fastlms.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class AdminCourseController extends BaseController{

    private final CourseService courseService;
    private final CategoryService categoryService;



    @GetMapping("/admin/course/list.do")
    public String list(Model model, CourseParam parameter){
        parameter.init();

        List<CourseDto> courseList = courseService.list(parameter);
        long totalCount =0;

        if (!CollectionUtils.isEmpty(courseList)){
            totalCount = courseList.get(0).getTotalCount();

        }


        String queryString = parameter.getQueryString();
        String pagerHtml =
                super.getPagerHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(), queryString);

        model.addAttribute("list",courseList);
        model.addAttribute("totalCount",totalCount);
        model.addAttribute("pager",pagerHtml);



        return "admin/course/list";

    }

    @GetMapping(value ={"/admin/course/add.do", "/admin/course/edit.do"})
    public String add(Model model, HttpServletRequest req
                ,CourseInput parameter
    ){
        //카테고리 정보
        model.addAttribute("category", categoryService.list());

        categoryService.list();

        boolean editMode = req.getRequestURI().contains("/edit.do");
        //이게 flase 라는거 아녀
        CourseDto detail = new CourseDto();

        System.out.println(editMode+": editMode");
        if (editMode){
            long id = parameter.getId();
            CourseDto existCourse = courseService.getById(id);
            if (existCourse ==null){
                // error 처리 하기
                model.addAttribute("message","강좌정보가 존재하지 않습니다");

                return "common/error";

            }
            detail = existCourse;


        }

        model.addAttribute("detail", detail);
        model.addAttribute("editMode",editMode);
        return "admin/course/add";

    }

    @PostMapping(value = {"/admin/curse/add.do", "/admin/course/edit.do"})
    public String addSubmit(Model model,
            HttpServletRequest req,CourseInput parameter){

        boolean editMode = req.getRequestURI().contains("/edit.do");
        if (editMode){
            long id =parameter.getId();
            CourseDto existCourse = courseService.getById(id);
            if (existCourse ==null){
                model.addAttribute("message","강좌 정보가 존재하지 않습니다");
                return "common/error";
            }
            // 수정 전용메서드
            boolean result = courseService.set(parameter);

        } else {
            boolean result = courseService.add(parameter);

        }

        return "redirect:/admin/course/list.do";

    }




}
