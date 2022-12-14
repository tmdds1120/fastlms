package com.zerobase.fastlms.admin.category.dto;


import com.zerobase.fastlms.admin.category.entity.Category;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class
CategoryDto {

    Long id;
    String categoryName;
    int sortValue;
    boolean usingYn;

    //ADD COLUMNS
    int courseCount;
    public static List<CategoryDto> of(List<Category> categories){
        if (categories!= null){
            List<CategoryDto> categoryList =new ArrayList<>();
            for (Category x : categories){
                categoryList.add(of(x));
            }
            return categoryList;
        }

        return null;
    }

    public static CategoryDto of(Category category){
        return CategoryDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .sortValue(category.getSortValue())
                .usingYn(category.isUsingYn())
                .build();
    }
}
