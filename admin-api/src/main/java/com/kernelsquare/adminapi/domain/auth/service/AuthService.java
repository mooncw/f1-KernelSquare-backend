package com.kernelsquare.adminapi.domain.auth.service;

import com.kernelsquare.adminapi.domain.auth.validation.AuthValidation;
import com.kernelsquare.domainmysql.domain.auth.command.AuthCommand;
import com.kernelsquare.domainmysql.domain.auth.info.AuthInfo;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.info.MemberInfo;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final MemberReader memberReader;
	private final AuthValidation authValidation;
	private final TokenProvider tokenProvider;

	@Transactional
	public AuthInfo.LoginInfo login(final AuthCommand.LoginMember command) {
		Member findMember = memberReader.findMember(command.email());

		authValidation.validatePassword(command.password(), findMember.getPassword());

		authValidation.validateAdminAuthority(findMember);

		return tokenProvider.createToken(MemberInfo.from(findMember));
	}
}
