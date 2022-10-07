package com.zerobase.fastlms.course.service.Impl;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.entity.Course;
import com.zerobase.fastlms.course.mapper.CourseMapper;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.repository.CourseRepository;
import com.zerobase.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class CourServiceImpl implements CourseService {



    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public boolean set(CourseInput parameter) {

        Optional<Course> optionalCourse =
                courseRepository.findById(parameter.getId());

        if (!optionalCourse.isPresent()){
            return false;
        }

        Course course = optionalCourse.get();
        course.setSubject(parameter.getSubject());
        course.setUdtDt(LocalDateTime.now());
        course.setKeyword(parameter.getKeyword());
        course.setSummary(parameter.getSummary());
        course.setContents(parameter.getContents());
        course.setPrice(parameter.getPrice());
        course.setSalePrice(parameter.getSalePrice());
        //종료 문자일
        course.setCategoryId(parameter.getCategoryId());
        courseRepository.save(course);

        return true;
    }

    // courseRepository 에 값을 저장 하는 메서드
    @Override
    public boolean add(CourseInput parameter) {
        Course course = Course.builder()
                //id 값은 자동으로 생성된다?
                // 값이 들어 갈때마나 우리가 identi 타입 설정을 해서 넣을때 마다 자동으로 설정
                .subject(parameter.getSubject())
                .keyword(parameter.getKeyword())
                .summary(parameter.getSummary())
                .contents(parameter.getContents())
                .price(parameter.getPrice())
                .salePrice(parameter.getSalePrice())
                // 종료일
                .categoryId(parameter.getCategoryId())
                .regDt(LocalDateTime.now())
                .build();

        courseRepository.save(course);

        return true;
    }

    @Override
    public List<CourseDto> list(CourseParam parameter) {
        long totalCount = courseMapper.selectListCount(parameter);

        List<CourseDto> list = courseMapper.selectList(parameter);
        if (!CollectionUtils.isEmpty(list)){
            int i= 0;
            for (CourseDto x :list) {
                x.setTotalCount(totalCount);

                x.setSeq(totalCount-parameter.getPageStart()-i);
                i++;
            }
        }
        return list;

    }

    @Override
    public CourseDto getById(long id) {

        return courseRepository.findById(id).map(CourseDto :: of).orElse(null);


    }
}
