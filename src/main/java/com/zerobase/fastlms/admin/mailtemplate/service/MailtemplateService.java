package com.zerobase.fastlms.admin.mailtemplate.service;

import com.zerobase.fastlms.admin.mailtemplate.dto.MailtemplateDto;
import com.zerobase.fastlms.admin.mailtemplate.model.MailtemplateInput;
import com.zerobase.fastlms.admin.mailtemplate.model.MailtemplateParam;

import java.util.List;

public interface MailtemplateService {
    
    /**
     * 템플릿 등록
     */
    boolean add(MailtemplateInput parameter);
    
    /**
     * 템플릿 수정
     */
    boolean set(MailtemplateInput parameter);
    
    /**
     * 템플릿 상세정보
     */
    MailtemplateDto getById(long id);
    
    List<MailtemplateDto> list(MailtemplateParam parameter);
    
}
