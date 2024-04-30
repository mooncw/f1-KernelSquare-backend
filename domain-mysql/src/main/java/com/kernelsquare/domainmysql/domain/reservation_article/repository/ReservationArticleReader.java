package com.kernelsquare.domainmysql.domain.reservation_article.repository;

import java.time.LocalDateTime;

public interface ReservationArticleReader {
    Long countBeforeMyReservationArticleEndTime(Long memberId, LocalDateTime currentTime);
}
