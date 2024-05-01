package com.kernelsquare.domainmysql.domain.member_answer_vote.repository;

import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;

public interface MemberAnswerVoteStore {
    void store(MemberAnswerVote memberAnswerVote);

    void delete(Long memberAnswerVoteId);
}
