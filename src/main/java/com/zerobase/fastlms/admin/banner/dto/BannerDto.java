package com.zerobase.fastlms.admin.banner.dto;

import com.zerobase.fastlms.admin.banner.entity.Banner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BannerDto {

    Long id;

    String altText;
    String bannerUrl;
    String target;
    String linkUrl;
    int sortValue;
    boolean pubYn;

    LocalDateTime regDt;

    String filename;
    String urlFilename;


    long totalCount;
    long seq;

    public static BannerDto of(Banner x) {
        if (x == null) {
            return null;
        }
        return BannerDto.builder()
            .id(x.getId())
            .altText(x.getAltText())
            .target(x.getTarget())
            .linkUrl(x.getLinkUrl())
            .sortValue(x.getSortValue())
            .pubYn(x.isPubYn())
            .filename(x.getFilename())
            .urlFilename(x.getUrlFilename())
            .regDt(x.getRegDt())
            .build();
    }

    public static List<BannerDto> of(List<Banner> xList) {
        if (xList == null) {
            return null;
        }

        List<BannerDto> bannerList = new ArrayList<>();
        for(Banner x : xList) {
            bannerList.add(BannerDto.of(x));
        }
        return bannerList;
    }

}

