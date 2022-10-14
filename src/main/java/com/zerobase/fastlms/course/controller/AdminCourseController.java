package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.service.CourseService;
import com.zerobase.fastlms.util.PageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Controller
@Slf4j
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
                getPagerHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(), queryString);

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



    @PostMapping("/admin/course/delete.do")
    public String del(Model model, HttpServletRequest req
    ,CourseInput parameter){

        boolean result =courseService.del(parameter.getIdList());


        return "redirect:/admin/course/list.do ";
    }

    private String[] getNewSaveFile(String baseLocalPath, String baseUrlPath, String originalFilename){
        LocalDate now = LocalDate.now();
        String[] dirs = {
                String.format("%s/%d/", baseLocalPath,now.getYear()),
                String.format("%s/%d/%02d/", baseLocalPath, now.getYear(),now.getMonthValue()),
                String.format("%s/%d/%02d/%02d/", baseLocalPath, now.getYear(), now.getMonthValue(), now.getDayOfMonth())};

        String urlDir = String.format("%s/%d/%02d/%02d/", baseUrlPath, now.getYear(), now.getMonthValue(), now.getDayOfMonth());

        for(String dir : dirs) {
            File file = new File(dir);
            if (!file.isDirectory()) {
                file.mkdir();
            }
        }

        String fileExtension="";
        if (originalFilename != null){
            int dotPos = originalFilename.lastIndexOf(".");
            if (dotPos > -1){
                fileExtension = originalFilename.substring(dotPos +1);
            }
        }

        String uuid = UUID.randomUUID().toString().replace("-","");
        String newFileName = String.format("%s%s", dirs[2], uuid);
        String newUrlFileName = String.format("%s%s",urlDir,uuid);
        if (fileExtension.length()>0){
            newFileName+="."+fileExtension;
            newUrlFileName+="."+fileExtension;

        }

        return new String[]{newFileName,newUrlFileName};

    }

    @PostMapping(value = {"/admin/course/add.do", "/admin/course/edit.do"})
    public String addSubmit(Model model,
                            HttpServletRequest req
                            , MultipartFile file, CourseInput parameter){

        String saveFileName = "";
        String urlFileName ="";
        if (file != null){
            String originalFilename = file.getOriginalFilename();
            String baseLocalPath = "/Users/";
            String baseUrlPath="/files";
            String[] arrFilename = getNewSaveFile(baseLocalPath,baseUrlPath,originalFilename);
            saveFileName = arrFilename[0];
            urlFileName = arrFilename[1];

            try{
                File newFile = new File(saveFileName);
                FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(newFile));
            } catch (IOException e){
                log.info("######################### -1");
                log.info(e.getMessage());
            }
        }

        parameter.setFilename(saveFileName);
        parameter.setUrlFilename(urlFileName);

        boolean editMode =req.getRequestURI().contains("/edit.do");

        // 단순 등록의 경우는 editMode 가 false 이니까 넘어간다
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
            //단순 코스 등록
            boolean result = courseService.add(parameter);

        }

        return "redirect:/admin/course/list.do";

    }



}
