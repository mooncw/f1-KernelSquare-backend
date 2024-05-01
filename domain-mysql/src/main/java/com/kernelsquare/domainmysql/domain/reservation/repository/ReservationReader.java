package com.kernelsquare.domainmysql.domain.reservation.repository;

import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationReader {
    Boolean existsMyReservationInReservationArticle(Long reservationArticleId, Long memberId);

    Boolean existsMyReservationSinceCurrentTime(Long memberId, LocalDateTime currentTime);

    List<Reservation> findAllInReservationArticle(Long articleId);

    List<Reservation> findAllByMember(Long memberId);

    Long countInReservationArticle(Long articleId);

    Long countAvailableInReservationArticle(Long articleId);

    Reservation find(Long reservationId);
}
