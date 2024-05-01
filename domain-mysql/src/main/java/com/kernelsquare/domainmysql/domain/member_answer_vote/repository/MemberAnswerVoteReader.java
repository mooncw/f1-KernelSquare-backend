package com.kernelsquare.domainmysql.domain.member_answer_vote.repository;

import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;

import java.util.List;

public interface MemberAnswerVoteReader {
    MemberAnswerVote findMyVoteInAnswer(Long memberId, Long answerId);

    Boolean existsMyVoteInAnswer(Long memberId, Long answerId);

    List<MemberAnswerVote> findAllByMemberId(Long memberId);
}
