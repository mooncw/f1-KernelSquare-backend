package com.kernelsquare.domainmysql.domain.reservation.repository;

import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationStoreImpl implements ReservationStore {
    private final ReservationRepository reservationRepository;

    @Override
    public void store(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public void delete(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    @Override
    public void deleteAllByReservationArticleId(Long reservationArticelId) {
        reservationRepository.deleteAllByReservationArticleId(reservationArticelId);
    }
}
