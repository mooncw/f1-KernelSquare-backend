package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LevelServiceStatus implements ServiceStatus {
    //error
    LEVEL_NOT_FOUND(1400),
    LEVEL_ALREADY_EXISTED(1401),

    //success
    LEVEL_CREATED(1440),
    LEVEL_FOUND(1441),
    LEVEL_DELETED(1442);

    private final Integer code;

    @Override
    public Integer getServiceStatus() { return code; }

}
