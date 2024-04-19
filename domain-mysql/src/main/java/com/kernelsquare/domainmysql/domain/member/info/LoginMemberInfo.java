package com.kernelsquare.domainmysql.domain.member.info;

import lombok.Getter;

@Getter
public class LoginMemberInfo {
    private Long memberId;
    private String password;

    public LoginMemberInfo(Long memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }
}
