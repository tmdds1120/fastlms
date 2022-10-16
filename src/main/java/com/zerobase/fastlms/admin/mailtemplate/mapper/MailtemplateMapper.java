package com.zerobase.fastlms.admin.mailtemplate.mapper;


import com.zerobase.fastlms.admin.mailtemplate.dto.MailtemplateDto;
import com.zerobase.fastlms.admin.mailtemplate.model.MailtemplateParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MailtemplateMapper {

    long selectListCount(MailtemplateParam parameter);
    List<MailtemplateDto> selectList(MailtemplateParam parameter);

}
