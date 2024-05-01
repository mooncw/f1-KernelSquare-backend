package com.kernelsquare.domainmysql.domain.reservation_article.repository;

import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;

import java.time.LocalDateTime;

public interface ReservationArticleReader {
    Long countBeforeMyReservationArticleEndTime(Long memberId, LocalDateTime currentTime);

    ReservationArticle find(Long reservationArticleId);
}
