package com.kernelsquare.domainmysql.domain.reservation.repository;

import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;

public interface ReservationStore {
    void store(Reservation reservation);

    void delete(Long reservationId);

    void deleteAllByReservationArticleId(Long reservationArticelId);
}
