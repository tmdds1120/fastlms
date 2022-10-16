package com.zerobase.fastlms.admin.mailtemplate.dto;

import com.zerobase.fastlms.admin.mailtemplate.entity.Mailtemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MailtemplateDto {
    
    long id;
    String mailtemplateKey;
    String title;
    String contents;
    
    LocalDateTime regDt;//등록일(추가날짜)
    LocalDateTime udtDt;//수정일(수정날짜)
    
    //추가컬럼
    long totalCount;
    long seq;
    
    public static MailtemplateDto of(Mailtemplate mailtemplate) {
        
        return MailtemplateDto.builder()
                .id(mailtemplate.getId())
                .mailtemplateKey(mailtemplate.getMailtemplateKey())
                .title(mailtemplate.getTitle())
                .contents(mailtemplate.getContents())
                .regDt(mailtemplate.getRegDt())
                .udtDt(mailtemplate.getUdtDt())
                .build();
    }
    
    
    public String getRegDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return regDt != null ? regDt.format(formatter) : "";
    }
    
    public String getUdtDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return udtDt != null ? udtDt.format(formatter) : "";
        
    }
    
}
