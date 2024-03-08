package com.kernelsquare.core.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CodingMeetingReadType {
    ALL("all"), OPEN("open"), CLOSED("closed"), OWNED("owned");

    private final String parameter;

    public String getParameter() {
        return parameter;
    }
}
