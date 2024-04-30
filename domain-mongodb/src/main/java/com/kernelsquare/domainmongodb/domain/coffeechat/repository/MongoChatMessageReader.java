package com.kernelsquare.domainmongodb.domain.coffeechat.repository;

import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;

import java.util.List;

public interface MongoChatMessageReader {
    List<MongoChatMessage> findAllByRoomKey(String roomKey);
}
