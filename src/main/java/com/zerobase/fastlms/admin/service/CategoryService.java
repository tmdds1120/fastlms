package com.zerobase.fastlms.admin.service;

import com.zerobase.fastlms.admin.dto.CategoryDto;

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

    boolean update(CategoryDto category);

    boolean del(long id);


}
