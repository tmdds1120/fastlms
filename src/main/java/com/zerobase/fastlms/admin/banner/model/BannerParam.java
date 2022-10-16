package com.zerobase.fastlms.admin.banner.model;

import com.zerobase.fastlms.common.model.CommonParam;
import lombok.Data;

@Data
// 페이지 인덱싱하는 CommonParam 상속, BannerParam 에서 쓸수 있게
public class BannerParam extends CommonParam {
    long id;
}
