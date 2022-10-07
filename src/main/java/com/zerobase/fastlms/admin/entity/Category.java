package com.zerobase.fastlms.admin.entity;


import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    String categoryName;
    int sortValue;
    boolean usingYn;
}
