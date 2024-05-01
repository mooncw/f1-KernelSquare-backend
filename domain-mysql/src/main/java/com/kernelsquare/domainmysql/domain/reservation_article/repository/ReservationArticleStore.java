package com.kernelsquare.domainmysql.domain.reservation_article.repository;

import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;

public interface ReservationArticleStore {
    ReservationArticle store(ReservationArticle reservationArticle);

    void delete(Long reservationArticleId);
}
