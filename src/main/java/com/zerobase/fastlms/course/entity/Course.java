package com.zerobase.fastlms.course.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false)
    Long id;

    Long categoryId;

    String imagePath;
    String keyword;
    String subject;


    @Column(length = 1000)
    String summary;

    @Lob
    String contents;

    long price;
    long salePrice;
    LocalDate saleEndDt;


    LocalDateTime regDt; //<-- 등록일(추가날짜)
    LocalDateTime udtDt;// <-- 수정일(수정날짜)

}
