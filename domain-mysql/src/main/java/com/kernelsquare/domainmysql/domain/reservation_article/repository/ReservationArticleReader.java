package com.kernelsquare.domainmysql.domain.reservation_article.repository;

import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ReservationArticleReader {
    Long countBeforeMyReservationArticleEndTime(Long memberId, LocalDateTime currentTime);

    ReservationArticle find(Long reservationArticleId);

    Page<ReservationArticle> findAllPage(Pageable pageable);
}
