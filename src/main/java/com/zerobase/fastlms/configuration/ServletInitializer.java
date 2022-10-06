package com.zerobase.fastlms.configuration;

import com.zerobase.fastlms.FastlmsApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    //https://www.koreanpsychology.or.kr
    // /user/sub01_3_1.asp --> 물리적으로 존재
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FastlmsApplication.class);
    }

}
