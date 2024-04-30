package com.kernelsquare.domainmysql.domain.reservation.repository;

import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationReaderImpl implements ReservationReader {
    private final ReservationRepository reservationRepository;

    @Override
    public Boolean existsMyReservationInReservationArticle(Long reservationArticleId, Long memberId) {
        return reservationRepository.existsByReservationArticleIdAndMemberId(reservationArticleId, memberId);
    }

    @Override
    public Boolean existsMyReservationSinceCurrentTime(Long memberId, LocalDateTime currentTime) {
        return reservationRepository.existsByMemberIdAndEndTimeAfter(memberId, currentTime);
    }

    @Override
    public List<Reservation> findAllInReservationArticle(Long articleId) {
        return reservationRepository.findAllByReservationArticleId(articleId);
    }

    @Override
    public List<Reservation> findAllByMember(Long memberId) {
        return reservationRepository.findAllByMemberId(memberId);
    }

    @Override
    public Long countInReservationArticle(Long articleId) {
        return reservationRepository.countAllByReservationArticleId(articleId);
    }

    @Override
    public Long countAvailableInReservationArticle(Long articleId) {
        return reservationRepository.countByReservationArticleIdAndMemberIdIsNull(articleId);
    }
}
