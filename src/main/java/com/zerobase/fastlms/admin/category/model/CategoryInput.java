package com.zerobase.fastlms.admin.category.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryInput {

    long id;

    //add 에서 필요
    String categoryName;

    //update 에서 필요한 요소
    int sortValue;
    boolean usingYn;
}
