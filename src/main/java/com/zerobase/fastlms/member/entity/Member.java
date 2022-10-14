package com.zerobase.fastlms.member.entity;


import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
public class Member implements MemberCode{

    @Id
    private String userId;
    private String userName;
    private String phone;
    private String password;
    private LocalDateTime regDt;
    //회원정보 수정일
    private LocalDateTime udtDt;


    //메일-> 인증후 서비스 이용가능하게
    // 회원에서 인증확인 여부의 다른 변수 필요함
    private boolean emailAuthYn;
    private String emailAuthKey;
    private LocalDateTime emailAuthDt;


    // 이 Key 가 일치 하는 사용자가 접속했을때 비밀번호를 초기화 하는 부분
    // ++ 상시 허용할수는 없고 시간에 대한 제약 필요) 유효기간
    private String resetPasswordKey;
    private LocalDateTime resetPasswordLimitDt;

    private boolean adminYn;

    private String userStatus; // 이용가능 상태, 정지상태

    private String zipcode;
    private String addr;
    private String addrDetail;

    private LocalDateTime lastLoginDt;

}





