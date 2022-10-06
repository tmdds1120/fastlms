package com.zerobase.fastlms.admin.service;

import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.model.CategoryInput;

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


}
