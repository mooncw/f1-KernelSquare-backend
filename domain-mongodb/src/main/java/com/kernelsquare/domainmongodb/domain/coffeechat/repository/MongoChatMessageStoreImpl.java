package com.kernelsquare.domainmongodb.domain.coffeechat.repository;

import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoChatMessageStoreImpl implements MongoChatMessageStore {
    private final MongoChatMessageRepository mongoChatMessageRepository;

    @Override
    public void store(MongoChatMessage mongoChatMessage) {
        mongoChatMessageRepository.save(mongoChatMessage);
    }
}
