package com.kernelsquare.adminapi.domain.auth.facade;

import com.kernelsquare.adminapi.domain.auth.dto.AuthDto;
import com.kernelsquare.adminapi.domain.auth.mapper.AuthDtoMapper;
import com.kernelsquare.adminapi.domain.auth.service.AuthService;
import com.kernelsquare.domainmysql.domain.auth.info.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {
    private final AuthService authService;
    private final AuthDtoMapper authDtoMapper;

    public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
        AuthInfo.LoginInfo loginInfo = authService.login(authDtoMapper.toCommand(request));
        return authDtoMapper.of(loginInfo);
    }
}
