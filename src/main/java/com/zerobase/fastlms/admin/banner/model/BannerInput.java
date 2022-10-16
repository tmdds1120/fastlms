package com.zerobase.fastlms.admin.banner.model;

import lombok.Data;

@Data
public class BannerInput {

    Long id;

    String altText;
    String bannerUrl;
    String target;
    String linkUrl;
    int sortValue;
    boolean pubYn;

    String filename;
    String urlFilename;

    //삭제를 위한
    String idList;


}
