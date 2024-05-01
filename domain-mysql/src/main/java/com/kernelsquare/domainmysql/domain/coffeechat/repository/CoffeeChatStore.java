package com.kernelsquare.domainmysql.domain.coffeechat.repository;

import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;

public interface CoffeeChatStore {
    ChatRoom store(ChatRoom chatRoom);

    void deleteWithReservationArticle(Long reservationArticleId);
}
