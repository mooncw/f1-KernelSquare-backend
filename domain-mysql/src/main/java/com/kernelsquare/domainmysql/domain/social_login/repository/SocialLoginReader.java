package com.kernelsquare.domainmysql.domain.social_login.repository;

import com.kernelsquare.core.type.SocialProvider;

public interface SocialLoginReader {
    Boolean existsSocialEmail(String email, SocialProvider socialProvider);
}
