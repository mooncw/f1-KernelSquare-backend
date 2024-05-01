package com.kernelsquare.domainmysql.domain.member_answer_vote.repository;

import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAnswerVoteStoreImpl implements MemberAnswerVoteStore {
    private final MemberAnswerVoteRepository memberAnswerVoteRepository;

    @Override
    public void store(MemberAnswerVote memberAnswerVote) {
        memberAnswerVoteRepository.save(memberAnswerVote);
    }

    @Override
    public void delete(Long memberAnswerVoteId) {
        memberAnswerVoteRepository.deleteById(memberAnswerVoteId);
    }
}
