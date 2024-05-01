package com.kernelsquare.domainmysql.domain.reservation_article.repository;

import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationArticleStoreImpl implements ReservationArticleStore {
    private final ReservationArticleRepository reservationArticleRepository;

    @Override
    public ReservationArticle store(ReservationArticle reservationArticle) {
        return reservationArticleRepository.save(reservationArticle);
    }

    @Override
    public void delete(Long reservationArticleId) {
        reservationArticleRepository.deleteById(reservationArticleId);
    }
}
