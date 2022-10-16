package com.zerobase.fastlms.admin.mailtemplate.service.impl;

import com.zerobase.fastlms.admin.category.mapper.CategoryMapper;
import com.zerobase.fastlms.admin.category.repository.CategoryRepository;
import com.zerobase.fastlms.admin.mailtemplate.dto.MailtemplateDto;
import com.zerobase.fastlms.admin.mailtemplate.entity.Mailtemplate;
import com.zerobase.fastlms.admin.mailtemplate.mapper.MailtemplateMapper;
import com.zerobase.fastlms.admin.mailtemplate.model.MailtemplateInput;
import com.zerobase.fastlms.admin.mailtemplate.model.MailtemplateParam;
import com.zerobase.fastlms.admin.mailtemplate.repository.MailtemplateRepository;
import com.zerobase.fastlms.admin.mailtemplate.service.MailtemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MailtemplateServiceImpl implements MailtemplateService {
    
    private final MailtemplateMapper mailtemplateMapper;
    private final MailtemplateRepository mailtemplateRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    
    private Sort getSortBySortValueDesc() {
        return Sort.by(Sort.Direction.DESC, "sortValue");
    }
    
    @Override
    public boolean add(MailtemplateInput parameter) {
        
        //TODO:메일템플릿키가 존재하는지 확인
        
        Mailtemplate mailtemplate = Mailtemplate.builder()
                .mailtemplateKey(parameter.getMailtemplateKey())
                .title(parameter.getTitle())
                .contents(parameter.getContents())
                .regDt(LocalDateTime.now())
                .build();
        mailtemplateRepository.save(mailtemplate);
        
        return true;
    }
    
    @Override
    public boolean set(MailtemplateInput parameter) {
        
        Optional<Mailtemplate> optionalMailtemplate = mailtemplateRepository.findById(parameter.getId());
        if (optionalMailtemplate.isPresent()) {
            Mailtemplate mailtemplate = optionalMailtemplate.get();
            //mailtemplate.setMailtemplateKey(parameter.getMailtemplateKey());
            mailtemplate.setTitle(parameter.getTitle());
            mailtemplate.setContents(parameter.getContents());
            mailtemplateRepository.save(mailtemplate);
        }
        
        return true;
    }
    
    @Override
    public MailtemplateDto getById(long id) {
        return mailtemplateRepository.findById(id).map(MailtemplateDto::of).orElse(null);
    }
    
    @Override
    public List<MailtemplateDto> list(MailtemplateParam parameter) {
       
        long totalCount = mailtemplateMapper.selectListCount(parameter);
        List<MailtemplateDto> list = mailtemplateMapper.selectList(parameter);
        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for(MailtemplateDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - parameter.getPageStart() - i);
                i++;
            }
        }
        
        return list;
    }
    
    
    
}
