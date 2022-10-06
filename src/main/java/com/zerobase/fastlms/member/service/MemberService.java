package com.zerobase.fastlms.member.service;


import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {
    boolean register(MemberInput parameter);

    /**
     * uuid에 해당하는 계정을 찾아서 활성화 합니다
     * @param uuid
     * @return
     */
    boolean emailAuth(String uuid);


    /**
     * 입력한 이메일로 비밀번호 초기화 정보를 전송
     * @param parameter
     * @return
     */
    boolean sendResetPassword(ResetPasswordInput parameter);


    /**
     * 입력 받은 UUID 에 대해 비밀번호로 초기화 함
     * @param uuid
     * @param password
     * @return
     */
    boolean resetPassword(String uuid, String password);


    boolean checkResetPassword(String uuid);


    /**
     * 회원의 목록을 리턴(단, 관리자에게만 혀용)
     *
     * @return
     */
    List<MemberDto> list(MemberParam parameter);

    /**
     *  회원상세 정보
     * @param userId
     * @return
     */
    MemberDto detail(String userId);


    /**
     * 회원 상태 변경
     * @param userId
     * @param userStatus
     * @return
     */
    boolean updateStatus(String userId, String userStatus);


}
