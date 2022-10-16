package com.zerobase.fastlms.admin.category.mapper;

import com.zerobase.fastlms.admin.category.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryDto> select(CategoryDto parameter);




}
