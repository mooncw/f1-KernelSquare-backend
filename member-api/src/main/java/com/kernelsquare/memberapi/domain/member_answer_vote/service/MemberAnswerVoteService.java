package com.kernelsquare.memberapi.domain.member_answer_vote.service;

import com.kernelsquare.core.common_response.error.code.MemberAnswerVoteErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerReader;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerStore;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernelsquare.domainmysql.domain.member_answer_vote.repository.MemberAnswerVoteReader;
import com.kernelsquare.domainmysql.domain.member_answer_vote.repository.MemberAnswerVoteStore;
import com.kernelsquare.memberapi.domain.member_answer_vote.dto.CreateMemberAnswerVoteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberAnswerVoteService {
	private final MemberReader memberReader;
	private final AnswerReader answerReader;
	private final AnswerStore answerStore;
	private final MemberAnswerVoteReader memberAnswerVoteReader;
	private final MemberAnswerVoteStore memberAnswerVoteStore;

	@Transactional
	public void createVote(CreateMemberAnswerVoteRequest createMemberAnswerVoteRequest, Long answerId) {
		Member member = memberReader.findMember(createMemberAnswerVoteRequest.memberId());

		Answer answer = answerReader.findAnswer(answerId);

		if (Objects.equals(answer.getMember().getId(), createMemberAnswerVoteRequest.memberId())) {
			throw new BusinessException(MemberAnswerVoteErrorCode.MEMBER_ANSWER_VOTE_SELF_IMPOSSIBLE);
		}

		if (memberAnswerVoteReader.existsMyVoteInAnswer(createMemberAnswerVoteRequest.memberId(), answerId)) {
			throw new BusinessException(MemberAnswerVoteErrorCode.MEMBER_ANSWER_VOTE_DUPLICATION);
		}

		MemberAnswerVote memberAnswerVote = CreateMemberAnswerVoteRequest.toEntity(
			createMemberAnswerVoteRequest, member, answer
		);
		memberAnswerVoteStore.store(memberAnswerVote);

		if ((long)createMemberAnswerVoteRequest.status() == 1) {
			answerStore.upVote(answerId);
			return;
		}
		answerStore.downVote(answerId);
	}

	@Transactional
	public void deleteVote(Long answerId) {
		Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
		MemberAnswerVote memberAnswerVote = memberAnswerVoteReader.findMyVoteInAnswer(memberId, answerId);

		memberAnswerVoteStore.delete(memberAnswerVote.getId());

		int memberAnswerVoteStatus = -memberAnswerVote.getStatus();
		if (memberAnswerVoteStatus == 1) {
			answerStore.upVote(answerId);
			return;
		}
		answerStore.downVote(answerId);
	}
}
