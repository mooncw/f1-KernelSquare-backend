package com.kernelsquare.domainmysql.domain.reservation_article.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReservationArticleReaderImpl implements ReservationArticleReader {
    private final ReservationArticleRepository reservationArticleRepository;

    @Override
    public Long countBeforeMyReservationArticleEndTime(Long memberId, LocalDateTime currentTime) {
        return reservationArticleRepository.countAllByMemberIdAndEndTimeBefore(memberId, currentTime);
    }
}
