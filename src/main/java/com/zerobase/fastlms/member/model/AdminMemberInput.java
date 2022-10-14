package com.zerobase.fastlms.member.model;


import lombok.Data;

@Data
public class AdminMemberInput {
    String userId;
    String userStatus;
    String password;
}
