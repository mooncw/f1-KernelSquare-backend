package com.kernelsquare.adminapi.domain.auth.mapper;

import com.kernelsquare.adminapi.domain.auth.dto.AuthDto;
import com.kernelsquare.adminapi.domain.auth.dto.TokenResponse;
import com.kernelsquare.domainmysql.domain.auth.command.AuthCommand;
import com.kernelsquare.domainmysql.domain.auth.info.AuthInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AuthDtoMapper {
    AuthCommand.LoginMember toCommand(AuthDto.LoginRequest request);

    @Mapping(target = "tokenDto", expression = "java(mapTokenDto(loginInfo))")
    AuthDto.LoginResponse of(AuthInfo.LoginInfo loginInfo);

    default TokenResponse mapTokenDto(AuthInfo.LoginInfo loginInfo) {
        return TokenResponse.of(loginInfo.accessToken(), loginInfo.refreshToken());
    }
}
