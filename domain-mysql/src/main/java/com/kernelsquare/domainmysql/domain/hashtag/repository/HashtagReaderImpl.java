package com.kernelsquare.domainmysql.domain.hashtag.repository;

import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HashtagReaderImpl implements HashtagReader {
    private final HashtagRepository hashtagRepository;

    @Override
    public List<Hashtag> findAll(Long articleId) {
        return hashtagRepository.findAllByReservationArticleId(articleId);
    }

    @Override
    public Long count(Long articleId) {
        return hashtagRepository.countAllByReservationArticleId(articleId);
    }
}
