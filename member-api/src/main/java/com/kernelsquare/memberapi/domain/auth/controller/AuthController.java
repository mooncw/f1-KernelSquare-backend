package com.kernelsquare.memberapi.domain.auth.controller;

import static com.kernelsquare.core.common_response.response.code.AuthResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.validation.ValidationSequence;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.memberapi.domain.auth.dto.CheckDuplicateEmailRequest;
import com.kernelsquare.memberapi.domain.auth.dto.CheckDuplicateNicknameRequest;
import com.kernelsquare.memberapi.domain.auth.dto.LoginRequest;
import com.kernelsquare.memberapi.domain.auth.dto.LoginResponse;
import com.kernelsquare.memberapi.domain.auth.dto.SignUpRequest;
import com.kernelsquare.memberapi.domain.auth.dto.SignUpResponse;
import com.kernelsquare.memberapi.domain.auth.dto.TokenRequest;
import com.kernelsquare.memberapi.domain.auth.dto.TokenResponse;
import com.kernelsquare.memberapi.domain.auth.service.AuthService;
import com.kernelsquare.memberapi.domain.auth.service.TokenProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final TokenProvider tokenProvider;

	@PostMapping("/auth/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(
		final @RequestBody @Validated(ValidationSequence.class) LoginRequest loginRequest) {
//		Member member = authService.login(loginRequest);
//		TokenResponse tokenResponse = tokenProvider.createToken(member, loginRequest);
//		LoginResponse loginResponse = LoginResponse.of(member, tokenResponse);
		return ResponseEntityFactory.toResponseEntity(LOGIN_SUCCESS, authService.login(loginRequest));
	}

	@PostMapping("/auth/signup")
	public ResponseEntity<ApiResponse<SignUpResponse>> signUp(
		final @RequestBody @Validated(ValidationSequence.class) SignUpRequest signUpRequest) {
		SignUpResponse signUpResponse = authService.signUp(signUpRequest);
		return ResponseEntityFactory.toResponseEntity(SIGN_UP_SUCCESS, signUpResponse);
	}

	@PostMapping("/auth/reissue")
	public ResponseEntity<ApiResponse<TokenResponse>> reissueAccessToken(
		final @RequestBody TokenRequest requestTokenRequest) {
		TokenResponse tokenResponse = tokenProvider.reissueToken(requestTokenRequest);
		return ResponseEntityFactory.toResponseEntity(ACCESS_TOKEN_REISSUED, tokenResponse);
	}

	@PostMapping("/auth/check/email")
	public ResponseEntity<ApiResponse> isEmailUnique(
		final @RequestBody @Validated(ValidationSequence.class) CheckDuplicateEmailRequest emailRequest) {
		authService.isEmailUnique(emailRequest.email());
		return ResponseEntityFactory.toResponseEntity(EMAIL_UNIQUE_VALIDATED);
	}

	@PostMapping("/auth/check/nickname")
	public ResponseEntity<ApiResponse> isNicknameUnique(
		final @RequestBody @Validated(ValidationSequence.class) CheckDuplicateNicknameRequest nicknameRequest) {
		authService.isNicknameUnique(nicknameRequest.nickname());
		return ResponseEntityFactory.toResponseEntity(NICKNAME_UNIQUE_VALIDATED);
	}

	@PostMapping("/auth/logout")
	public ResponseEntity<ApiResponse> logout(final @RequestBody TokenRequest tokenRequest) {
		tokenProvider.logout(tokenRequest);
		return ResponseEntityFactory.toResponseEntity(LOGOUT_SUCCESS);
	}
}