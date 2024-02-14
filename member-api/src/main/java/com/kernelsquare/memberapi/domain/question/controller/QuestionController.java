package com.kernelsquare.memberapi.domain.question.controller;

import static com.kernelsquare.core.common_response.response.code.QuestionResponseCode.*;

import com.kernelsquare.memberapi.domain.auth.dto.MemberPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.memberapi.domain.question.dto.CreateQuestionRequest;
import com.kernelsquare.memberapi.domain.question.dto.CreateQuestionResponse;
import com.kernelsquare.memberapi.domain.question.dto.FindQuestionResponse;
import com.kernelsquare.memberapi.domain.question.dto.UpdateQuestionRequest;
import com.kernelsquare.memberapi.domain.question.service.QuestionService;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.dto.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QuestionController {
	private final QuestionService questionService;

	@PostMapping("/questions")
	public ResponseEntity<ApiResponse<CreateQuestionResponse>> createQuestion(
		@AuthenticationPrincipal
		MemberPrincipal memberPrincipal,
		@Valid
		@RequestBody
		CreateQuestionRequest createQuestionRequest
	) {

		log.info("@AuthenticationPrincipal : " + memberPrincipal);
		CreateQuestionResponse createQuestionResponse = questionService.createQuestion(createQuestionRequest, memberPrincipal.getMember());

		return ResponseEntityFactory.toResponseEntity(QUESTION_CREATED, createQuestionResponse);
	}

	@GetMapping("/questions/{questionId}")
	public ResponseEntity<ApiResponse<FindQuestionResponse>> findQuestion(
		@PathVariable
		Long questionId
	) {
		FindQuestionResponse findQuestionResponse = questionService.findQuestion(questionId);

		return ResponseEntityFactory.toResponseEntity(QUESTION_FOUND, findQuestionResponse);
	}

	@GetMapping("questions")
	public ResponseEntity<ApiResponse<PageResponse<FindQuestionResponse>>> findAllQuestions(
		@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		PageResponse<FindQuestionResponse> pageResponse = questionService.findAllQuestions(pageable);

		return ResponseEntityFactory.toResponseEntity(QUESTION_ALL_FOUND, pageResponse);
	}

	@PutMapping("/questions/{questionId}")
	public ResponseEntity<ApiResponse> updateQuestion(
		@PathVariable
		Long questionId,
		@Valid
		@RequestBody
		UpdateQuestionRequest updateQuestionRequest
	) {
		questionService.updateQuestion(questionId, updateQuestionRequest);

		return ResponseEntityFactory.toResponseEntity(QUESTION_UPDATED);
	}

	@DeleteMapping("/questions/{questionId}")
	public ResponseEntity<ApiResponse> deleteQuestion(
		@PathVariable
		Long questionId
	) {
		questionService.deleteQuestion(questionId);

		return ResponseEntityFactory.toResponseEntity(QUESTION_DELETED);
	}
}
