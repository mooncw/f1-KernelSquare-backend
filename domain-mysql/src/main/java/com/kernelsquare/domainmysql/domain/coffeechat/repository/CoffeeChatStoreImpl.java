package com.kernelsquare.domainmysql.domain.coffeechat.repository;

import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CoffeeChatStoreImpl implements CoffeeChatStore {
    private final CoffeeChatRepository coffeeChatRepository;

    @Override
    public ChatRoom store(ChatRoom chatRoom) {
        return coffeeChatRepository.save(chatRoom);
    }

    @Override
    public void deleteWithReservationArticle(Long reservationArticleId) {
        coffeeChatRepository.deleteChatRoom(reservationArticleId);
    }
}
