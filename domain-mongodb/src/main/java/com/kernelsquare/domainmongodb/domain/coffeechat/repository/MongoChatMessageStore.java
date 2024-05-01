package com.kernelsquare.domainmongodb.domain.coffeechat.repository;

import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;

public interface MongoChatMessageStore {
    void store(MongoChatMessage mongoChatMessage);
}
