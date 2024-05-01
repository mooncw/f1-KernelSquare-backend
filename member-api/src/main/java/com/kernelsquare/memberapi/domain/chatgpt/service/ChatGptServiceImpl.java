package com.kernelsquare.memberapi.domain.chatgpt.service;

import com.kernelsquare.core.common_response.error.code.KernelSquareBotErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.constants.KernelSquareBotConstants;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.answer.info.AnswerInfo;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerReader;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerStore;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question.repository.QuestionReader;
import com.kernelsquare.memberapi.domain.chatgpt.client.ChatGptClient;
import com.kernelsquare.memberapi.domain.chatgpt.dto.ChatGptDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class ChatGptServiceImpl implements ChatGptService {

	private final ChatGptClient chatGptClient;
	private final AnswerReader answerReader;
	private final AnswerStore answerStore;
	private final QuestionReader questionReader;
	private final MemberReader memberReader;

	@Value("${chatgpt.rest-api-key}")
	private String apiKey;

	@Value("${chatgpt.chat-api-url}")
	private String chatApiUrl;

	@SneakyThrows({URISyntaxException.class})
	@Transactional
	@Override
	public AnswerInfo createChatGptAnswer(Long questionId) {
		if (answerReader.existsMyAnswerInQuestion(KernelSquareBotConstants.KERNEL_SQUARE_AI_INTERN, questionId)) {
			throw new BusinessException(KernelSquareBotErrorCode.ANSWER_ALREADY_EXIST);
		}

		Question question = questionReader.findQuestion(questionId);
		Member member = memberReader.findMemberWithNickname(KernelSquareBotConstants.KERNEL_SQUARE_AI_INTERN);

		ChatGptDto.CreateAnswerRequest answerRequest = ChatGptDto.CreateAnswerRequest.of(question);

		ChatGptDto.CreateAnswerResponse chatGptResponse = chatGptClient.getChatGptResponse(new URI(chatApiUrl),
			"Bearer " + apiKey, answerRequest);

		String response = chatGptResponse.getText()
			.orElseThrow(() -> new BusinessException(KernelSquareBotErrorCode.EMPTY_ANSWER_RESPONSE));

		Answer answer = Answer.builder()
			.question(question)
			.imageUrl(null)
			.voteCount(0L)
			.content(response)
			.member(member)
			.build();

		answerStore.store(answer);

		return AnswerInfo.from(question, member);
	}
}
