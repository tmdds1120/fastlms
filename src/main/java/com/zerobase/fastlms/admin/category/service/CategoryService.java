package com.zerobase.fastlms.admin.category.service;

import com.zerobase.fastlms.admin.category.dto.CategoryDto;
import com.zerobase.fastlms.admin.category.model.CategoryInput;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> list();



    /**
     * 카테고리 신규 추가
     *
     * 카테고리 수정
     *
     * 카테고리 삭제
     */
    boolean add(String categoryName);


    // 카테고리 Input으로 부터 데이터 받기
    boolean update(CategoryInput category);

    boolean del(long id);


    /**
     * 프론트 카테고리 정보
     * @param parameter
     * @return
     */
    List<CategoryDto> frontList(CategoryDto parameter);
}
