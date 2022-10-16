package com.zerobase.fastlms.admin.mailtemplate.model;

import lombok.Data;

@Data
public class MailtemplateInput {
    
    long id;
    String mailtemplateKey;
    String title;
    String contents;

}
