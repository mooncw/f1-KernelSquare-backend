package com.kernelsquare.domainmysql.domain.member.repository;

import com.kernelsquare.core.common_response.error.code.AuthErrorCode;
import com.kernelsquare.core.common_response.error.code.KernelSquareBotErrorCode;
import com.kernelsquare.core.common_response.error.code.MemberErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberReaderImpl implements MemberReader{
    private final MemberRepository memberRepository;
    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public Member findMember(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(AuthErrorCode.INVALID_ACCOUNT));
    }

    @Override
    public Member findMemberWithNickname(String nickname) {
        return memberRepository.findByNickname(nickname)
            .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public Boolean existsMemberEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsMemberNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
