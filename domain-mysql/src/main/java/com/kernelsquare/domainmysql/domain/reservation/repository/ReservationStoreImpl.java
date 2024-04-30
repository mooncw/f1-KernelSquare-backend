package com.kernelsquare.domainmysql.domain.reservation.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationStoreImpl implements ReservationStore {
    private final ReservationRepository reservationRepository;

    @Override
    public void deleteAllByReservationArticleId(Long reservationArticelId) {
        reservationRepository.deleteAllByReservationArticleId(reservationArticelId);
    }
}
