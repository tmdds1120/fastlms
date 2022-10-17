package com.zerobase.fastlms.member.service.impl;


import com.zerobase.fastlms.admin.mailtemplate.entity.Mailtemplate;
import com.zerobase.fastlms.admin.mailtemplate.repository.MailtemplateRepository;
import com.zerobase.fastlms.member.mapper.MemberMapper;
import com.zerobase.fastlms.admin.member.model.MemberParam;
import com.zerobase.fastlms.common.components.MailComponents;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.member.dto.MemberDto;
import com.zerobase.fastlms.member.entity.Member;
import com.zerobase.fastlms.member.entity.MemberCode;
import com.zerobase.fastlms.member.exception.MemberNotEmailAuthException;
import com.zerobase.fastlms.member.exception.MemberStopUserException;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import com.zerobase.fastlms.member.repository.MemberLoginHistoryRepository;
import com.zerobase.fastlms.member.repository.MemberRepository;
import com.zerobase.fastlms.member.service.MemberService;
import com.zerobase.fastlms.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;
    private final MailtemplateRepository mailtemplateRepository;

    private final MemberMapper memberMapper;

    private final MemberLoginHistoryRepository memberLoginHistoryRepository;

    /**
     *  화원 가입
     *  1. 중복 검증
     *  2. 회원 저장
     *
     */
    @Override
    public boolean register(MemberInput parameter) {


        Optional<Member> optionalMember= memberRepository.findById(parameter.getUserId());
        if (optionalMember.isPresent()){
            // 현재 userId 에 해당하는 데이터 존재한다
            //이 이 경우 에는 탈출 해야한다

            return false;
        }
        // 패스워드 해쉬에 대한 저장
        String encPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());

        String uuid = UUID.randomUUID().toString();

        Member member =Member.builder()
                .userId(parameter.getUserId())
                .userName(parameter.getUserName())
                .phone(parameter.getPhone())
                .password(encPassword)
                .regDt(LocalDateTime.now())
                .emailAuthYn(false)
                .emailAuthKey(uuid)
                .userStatus(Member.MEMBER_STATUS_REQ)
                .build();
        memberRepository.save(member);

        mailtemplateRepository.findByMailtemplateKey(Mailtemplate.MAILTEMPLATE_KEY_MEMBER_REGISTER).ifPresent(e -> {
            String email = parameter.getUserId();
            String subject = e.getTitle();
            String text = e.getContents().replace("####URL_AUTH####", "http://localhost:8080/member/email-auth?id=" + uuid);
            mailComponents.sendMail(email, subject, text);
        });

//        String email = parameter.getUserId();
//        String subject = "fastlms 사이트 가입을 축하드립니다";
//        String text = "<p>fastlms 사이트 가입축하 text 문 </p>"
//                +"<p>아래 링크를 클릭하셔서 가입을 완료하세요</p>"
//                +"<div><a href=\"http://localhost:8080/member/email-auth?id="+uuid+"\""
//                        +">가입완료</a></div>";

//        mailComponents.sendMail(email,subject,text);

        return true;
    }


    /**
     * 이메일 인증을 통한 로그인 활성화
     * 1. 한 번 활성화 이후 이것을 막을 것이냐
     * 2. 아니면 계속 시도하는걸 허용할 것이냐
     * @param uuid
     * @return
     */
    @Override
    public boolean emailAuth(String uuid) {
        Optional<Member> optionalMember =
        memberRepository.findByEmailAuthKey(uuid);

        if (!optionalMember.isPresent()){
            return  false;
        }

        Member member = optionalMember.get();
        if(member.isEmailAuthYn()){
            return false;
        }
        member.setUserStatus(Member.MEMBER_STATUS_ING);
        member.setEmailAuthYn(true);
        member.setEmailAuthDt(LocalDateTime.now());
        memberRepository.save(member);


        return true;
    }

    @Override
    public boolean sendResetPassword(ResetPasswordInput parameter) {

        Optional<Member> optionalMember =
                memberRepository.findByUserIdAndUserName(parameter.getUserId(), parameter.getUserName());

        if (!optionalMember.isPresent()){
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다");
        }
        String uuid = UUID.randomUUID().toString();

        Member member = optionalMember.get();
        member.setResetPasswordKey(uuid);
        member.setResetPasswordLimitDt(LocalDateTime.now().plusDays(1));
        memberRepository.save(member);

//        String email = parameter.getUserId();
//        String subject = "[fastlms] 비밀번호 초기화 메일입니다";
//        String text ="<p>fastlms 비밀번호 초기화 메일 입니다</p>" +
//                "<p>아래 링크를 클릭하여 비밀번호를 초기화 해주세요.</p>"+
//                "<div><a target='_blank' href='http://localhost:8080/member/reset/password?id="+uuid+"'>비밀번호 초기화 링크</div>";
//        mailComponents.sendMail(email,subject,text);

        mailtemplateRepository.findByMailtemplateKey(Mailtemplate.MAILTEMPLATE_KEY_MEMBER_RESET).ifPresent(e -> {
            String email = parameter.getUserId();
            String subject = e.getTitle();
            String text = e.getContents().replace("####URL_AUTH####", "http://localhost:8080/member/reset/password?id=" + uuid);
            mailComponents.sendMail(email, subject, text);
        });

        return true;
    }

    @Override
    public boolean resetPassword(String uuid, String password) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
        if (!optionalMember.isPresent()){
            throw new UsernameNotFoundException("회원정보가 존재하지 않숩니다");
        }
        Member member = optionalMember.get();

        if (member.getResetPasswordLimitDt() == null){
            throw new RuntimeException("유효한 날짜가 아닙니다. ");
        }

        if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }

        String encPassword =BCrypt.hashpw(password,BCrypt.gensalt());
        member.setPassword(encPassword);
        member.setResetPasswordKey("");
        member.setResetPasswordLimitDt(null);
        memberRepository.save(member);

        return true;
    }

    /**
     * 링크를 통해서 받아온 UUID 의 값이 유효한지 검증이 필요함
     * 왜냐하면 ?
     * @param uuid
     * @return
     */
    @Override
    public boolean checkResetPassword(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);
        if (!optionalMember.isPresent()){
            return false;
        }
        Member member = optionalMember.get();

        if (member.getResetPasswordLimitDt()==null){
            throw new RuntimeException("유효한 날짜가 아닙니다");
        }
        if (member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("유효한 날짜가 아닙니다");
        }
        return true;

    }

    @Override
    public List<MemberDto> list(MemberParam parameter) {

        long totalCount = memberMapper.selectListCount(parameter);
        List<MemberDto> list = memberMapper.selectList(parameter);
        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for(MemberDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public MemberDto detail(String userId) {
        //Member 는 row 한 데이터 -> dto 로 가공을해서  보낸다
        Optional<Member> optionalMember  = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return null;
        }

        Member member = optionalMember.get();
        MemberDto memberDto = MemberDto.of(member);
        System.out.println(memberDto.toString());
        System.out.println(member.toString());
        memberLoginHistoryRepository.findByUserId(memberDto.getUserId()).ifPresent(e -> {
            memberDto.setLoginHistoryList(e);
        });

        return memberDto;
    }

    @Override
    public boolean updateStatus(String userId, String userStatus) {


    Optional<Member> optionalMember = memberRepository.findById(userId);
    if  (!optionalMember.isPresent()){
        throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다. ");
    }

    Member member = optionalMember.get();

    member.setUserStatus(userStatus);
    memberRepository.save(member);


        return true;
    }

    @Override
    public boolean updatePassword(String userId, String password) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()){
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다");
        }

        Member member = optionalMember.get();

        String endPassword = BCrypt.hashpw(password,BCrypt.gensalt());
        member.setPassword(endPassword);

        memberRepository.save(member);

        return true;
    }

    @Override
    public ServiceResult updateMember(MemberInput parameter) {
        String userId = parameter.getUserId();

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();


        member.setPhone(parameter.getPhone());
        member.setZipcode(parameter.getZipcode());
        member.setAddr(parameter.getAddr());
        member.setAddrDetail(parameter.getAddrDetail());
        member.setUdtDt(LocalDateTime.now());
        memberRepository.save(member);

        return new ServiceResult();

    }

    @Override
    public ServiceResult updateMemberPassword(MemberInput parameter) {
        String userId = parameter.getUserId();

        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()) {
            return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
        }
        Member member = optionalMember.get();

        if (!PasswordUtils.equals(parameter.getPassword(), member.getPassword())){
            return new ServiceResult(false,"비밀번호가 일치하지 않습니다");
        }

        String encPassword = PasswordUtils.encPassword(parameter.getNewPassword());
        member.setPassword(encPassword);
        memberRepository.save(member);

        return new ServiceResult(true);
    }


    /**
     *
     * @param username == userEmail
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username){


        Optional<Member> optionalMember = memberRepository.findById(username);
        if (!optionalMember.isPresent()){
            throw new UsernameNotFoundException("회원정보가 존재하지 않습니다");
        }
        Member member = optionalMember.get();
        if (Member.MEMBER_STATUS_REQ.equals(member.getUserStatus())){
            throw new MemberNotEmailAuthException("이메일을 활성화 이후에 로그인을 해주세요");
//            org.springframework.security.authentication.InternalAuthenticationServiceException: 이메일을 활성화 이후에 로그인을 해주세요
        }
        if (Member.MEMBER_STATUS_STOP.equals(member.getUserStatus())){
            throw new MemberStopUserException("정지된 회원 입니다.");
        }

        if (Member.MEMBER_STATUS_WITHDRAW.equals(member.getUserStatus())){
            throw new MemberStopUserException("탈퇴된 회원 입니다");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));


        if(member.isAdminYn()){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        }
        // 스프링 시큐리티의 userDetail 에 id, pw, 역할 세가지 넘기기
        return new User(member.getUserId(),member.getPassword(),grantedAuthorities);


    }


    @Override
    public ServiceResult withdraw(String userId, String password) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if (!optionalMember.isPresent()){
            return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();

        if (!PasswordUtils.equals(password,member.getPassword())){
            return new ServiceResult(false, "비밀번호가 일치하지 않습니다");
        }

        member.setUserName("삭제회원");
        member.setPhone("");
        member.setPassword("");
        member.setRegDt(null);
        member.setUdtDt(null);
        member.setEmailAuthYn(false);
        member.setEmailAuthDt(null);
        member.setEmailAuthKey("");
        member.setResetPasswordKey("");
        member.setResetPasswordLimitDt(null);
        member.setUserStatus(MemberCode.MEMBER_STATUS_WITHDRAW);
        member.setZipcode("");
        member.setAddr("");
        member.setAddrDetail("");
        memberRepository.save(member);

        return new ServiceResult();
    }
}

