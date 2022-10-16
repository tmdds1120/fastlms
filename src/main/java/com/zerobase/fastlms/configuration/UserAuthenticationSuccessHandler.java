package com.zerobase.fastlms.configuration;

import com.zerobase.fastlms.member.entity.MemberLoginHistory;
import com.zerobase.fastlms.member.repository.MemberLoginHistoryRepository;
import com.zerobase.fastlms.member.repository.MemberRepository;
import com.zerobase.fastlms.util.RequestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    private final MemberLoginHistoryRepository memberLoginHistoryRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {

        String userId = authentication.getName();

        memberRepository.findById(userId).ifPresent(e -> {
            e.setLastLoginDt(LocalDateTime.now());
            memberRepository.save(e);
        });

        String userAgent = RequestUtils.getUserAgent(req);
        String clientIp = RequestUtils.getClientIP(req);
        memberLoginHistoryRepository.save(MemberLoginHistory.builder()
                .ipAddr(clientIp)
                .userAgent(userAgent)
                .userId(userId)
                .regDt(LocalDateTime.now())
                .build());

        super.onAuthenticationSuccess(req, resp, authentication);
    }
}
