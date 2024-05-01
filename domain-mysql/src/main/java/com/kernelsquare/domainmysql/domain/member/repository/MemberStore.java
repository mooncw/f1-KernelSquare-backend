package com.kernelsquare.domainmysql.domain.member.repository;

import com.kernelsquare.domainmysql.domain.member.entity.Member;

public interface MemberStore {
    Member store(Member member);

    void deleteMember(Long memberId);
}
