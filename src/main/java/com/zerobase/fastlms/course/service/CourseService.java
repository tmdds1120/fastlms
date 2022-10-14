package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseInput;

import java.util.List;

public interface CourseService {


    /**
     * 강좌 정보 수정
     */
    boolean set(CourseInput parameter);

    /**
     * 강좌 등록
     *
     *
     */
    boolean add(CourseInput parameter);


    /**
     * 강좌 목록
     * @param parameter
     * @return
     */
    List<CourseDto> list(CourseParam parameter);


    /**
     * 강좌 상세 정보
     */
    CourseDto getById(long id);


    /**
     * 강좌 내용 삭제
     * @param idList
     *
     */
    boolean del(String idList);


    /**
     * 강좌에 대해 관리자가 판매를 정지 할수 잇고 결국 같은 내용이 될수 없다
     * 그래서 다르게 설정?
     */
     List<CourseDto> frontList(CourseParam parameter);


    /**
     * 프론트 강좌 상세 정보
     * @param id
     * @return
     */
    CourseDto frontDetail(long id);

    /**
     * 수강 신청
     */

    ServiceResult req(TakeCourseInput parameter);


    List<CourseDto> listAll();
}
