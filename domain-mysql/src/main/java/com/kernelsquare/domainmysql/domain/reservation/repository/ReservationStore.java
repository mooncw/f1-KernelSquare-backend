package com.kernelsquare.domainmysql.domain.reservation.repository;

public interface ReservationStore {
    void deleteAllByReservationArticleId(Long reservationArticelId);
}
