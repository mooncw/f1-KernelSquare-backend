package com.kernelsquare.adminapi.domain.auth.service;

import com.kernelsquare.adminapi.domain.auth.validation.AuthValidation;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.auth.command.AuthCommand;
import com.kernelsquare.domainmysql.domain.auth.info.AuthInfo;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.info.MemberInfo;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("인증 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
	@InjectMocks
	private AuthService authService;
	@Mock
	private MemberReader memberReader;

	@Mock
	private AuthValidation authValidation;

	@Mock
	private TokenProvider tokenProvider;

	@Spy
	private PasswordEncoder passwordEncoder = Mockito.spy(BCryptPasswordEncoder.class);

	@Test
	@DisplayName("로그인 테스트")
	void testLogIn() throws Exception {
		//given
		String testEmail = "inthemeantime@name.com";
		String testPassword = "Letmego1!";

		Level level = Level.builder()
			.id(1L)
			.name(1L)
			.imageUrl("s3:whatever")
			.levelUpperLimit(500L)
			.build();

		Member member = Member.builder()
			.id(1L)
			.email(testEmail)
			.introduction("idontwannnaknow")
			.nickname("notnow")
			.password(passwordEncoder.encode(testPassword))
			.experience(1000L)
			.level(level)
			.imageUrl("s3:myface")
			.authorities(List.of(
				MemberAuthority.builder()
					.member(Member.builder().build())
					.authority(Authority.builder().authorityType(AuthorityType.ROLE_ADMIN).build())
					.build()))
			.build();

		AuthCommand.LoginMember command = AuthCommand.LoginMember.builder()
			.email(testEmail)
			.password(testPassword)
			.build();

		doNothing()
			.when(authValidation)
			.validatePassword(anyString(), anyString());

		doNothing()
			.when(authValidation)
			.validateAdminAuthority(any(Member.class));

		doReturn(member)
			.when(memberReader)
			.findMember(anyString());

		List<String> roles = member.getAuthorities().stream()
			.map(MemberAuthority::getAuthority)
			.map(Authority::getAuthorityType)
			.map(AuthorityType::getDescription)
			.toList();

		String accessToken = "dawdawdawd";
		String refreshToken = "ghsefaefaseg";

		AuthInfo.LoginInfo loginInfo = AuthInfo.LoginInfo.of(MemberInfo.from(member), roles, accessToken, refreshToken);

		doReturn(loginInfo)
			.when(tokenProvider)
			.createToken(any(MemberInfo.class));

		//when
		AuthInfo.LoginInfo memberInfo = authService.login(command);

		//then
		assertThat(memberInfo.memberId()).isEqualTo(member.getId());
		assertThat(memberInfo.level()).isEqualTo(member.getLevel().getName());
		assertThat(memberInfo.imageUrl()).isEqualTo(ImageUtils.makeImageUrl(member.getImageUrl()));
		assertThat(memberInfo.introduction()).isEqualTo(member.getIntroduction());
		assertThat(memberInfo.experience()).isEqualTo(member.getExperience());
		assertThat(memberInfo.nickname()).isEqualTo(member.getNickname());
		assertThat(memberInfo.roles()).isEqualTo(roles);
		assertThat(memberInfo.accessToken()).isEqualTo(accessToken);
		assertThat(memberInfo.refreshToken()).isEqualTo(refreshToken);

		//verify
		verify(memberReader, only()).findMember(anyString());
		verify(memberReader, times(1)).findMember(anyString());
		verify(authValidation, times(1)).validatePassword(anyString(), anyString());
		verify(authValidation, times(1)).validateAdminAuthority(any(Member.class));
		verify(tokenProvider, only()).createToken(any(MemberInfo.class));
		verify(tokenProvider, times(1)).createToken(any(MemberInfo.class));
	}

}
