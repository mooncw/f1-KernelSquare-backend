package com.kernelsquare.domainmysql.domain.social_login.repository;

import com.kernelsquare.core.type.SocialProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialLoginReaderImpl implements SocialLoginReader {
    private final SocialLoginRepository socialLoginRepository;

    @Override
    public Boolean existsSocialEmail(String email, SocialProvider socialProvider) {
        return socialLoginRepository.existsByEmailAndSocialProvider(email, socialProvider);
    }
}
