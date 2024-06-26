package com.kernelsquare.domainmysql.domain.member.repository;

import com.kernelsquare.domainmysql.domain.member.entity.Member;

public interface MemberReader {
    Member findMember(Long memberId);

    Member findMember(String email);

    Member findMemberWithNickname(String nickname);

    Boolean existsMemberEmail(String email);

    Boolean existsMemberNickname(String nickname);
}
