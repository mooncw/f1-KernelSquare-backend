package com.kernelsquare.domainmongodb.domain.coffeechat.repository;

import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MongoChatMessageReaderImpl implements MongoChatMessageReader {
    private final MongoChatMessageRepository mongoChatMessageRepository;

    @Override
    public List<MongoChatMessage> findAllByRoomKey(String roomKey) {
        return mongoChatMessageRepository.findAllByRoomKey(roomKey);
    }
}
