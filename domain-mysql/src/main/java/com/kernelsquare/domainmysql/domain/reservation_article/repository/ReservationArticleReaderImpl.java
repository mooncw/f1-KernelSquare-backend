package com.kernelsquare.domainmysql.domain.reservation_article.repository;

import com.kernelsquare.core.common_response.error.code.ReservationErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public ReservationArticle find(Long reservationArticleId) {
        return reservationArticleRepository.findById(reservationArticleId)
            .orElseThrow(() -> new BusinessException(ReservationErrorCode.RESERVATION_ARTICLE_NOT_FOUND));
    }

    @Override
    public Page<ReservationArticle> findAllPage(Pageable pageable) {
        return reservationArticleRepository.findAll(pageable);
    }
}
