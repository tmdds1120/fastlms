package com.zerobase.fastlms.admin.banner.service;

import com.zerobase.fastlms.admin.banner.dto.BannerDto;
import com.zerobase.fastlms.admin.banner.model.BannerInput;
import com.zerobase.fastlms.admin.banner.model.BannerParam;

import java.util.List;

public interface BannerService {

    /**
     * 배너 등록
     */
    boolean add(BannerInput parameter);
    
    /**
     * 배너 정보수정
     */
    boolean set(BannerInput parameter);
    
    /**
     * 배너 목록
     */
    List<BannerDto> list(BannerParam parameter);
    
    /**
     * 배너 상세정보
     */
    BannerDto getById(long id);
    
    /**
     * 배너 내용 삭제
     */
    boolean del(String idList);
    
    /**
     * 프론트 강좌 목록
     */
    List<BannerDto> frontList();

}
