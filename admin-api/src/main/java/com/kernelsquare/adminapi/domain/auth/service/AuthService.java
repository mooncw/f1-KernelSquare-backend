package com.kernelsquare.adminapi.domain.auth.service;

import com.kernelsquare.adminapi.domain.auth.validation.AuthValidation;
import com.kernelsquare.core.common_response.error.code.LevelErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.util.ExperiencePolicy;
import com.kernelsquare.domainmysql.domain.auth.command.AuthCommand;
import com.kernelsquare.domainmysql.domain.auth.info.AuthInfo;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelRepository;
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
	private final LevelRepository levelRepository;
	private final AuthValidation authValidation;
	private final TokenProvider tokenProvider;

	@Transactional
	public AuthInfo.LoginInfo login(final AuthCommand.LoginMember command) {
		Member findMember = memberReader.findMember(command.email());

		authValidation.validatePassword(command.password(), findMember.getPassword());

		addExperience(findMember);

		return tokenProvider.createToken(MemberInfo.from(findMember));
	}

	private void addExperience(Member member) {
		member.addExperience(ExperiencePolicy.MEMBER_DAILY_ATTENDED.getReward());
		if (member.isExperienceExceed(member.getExperience())) {
			member.updateExperience(member.getExperience() - member.getLevel().getLevelUpperLimit());
			Level nextLevel = levelRepository.findByName(member.getLevel().getName() + 1)
				.orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));
			member.updateLevel(nextLevel);
		}
	}
}
