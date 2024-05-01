package com.kernelsquare.domainmysql.domain.hashtag.repository;

import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HashtagStoreImpl implements HashtagStore {
    private final HashtagRepository hashtagRepository;

    @Override
    public void store(Hashtag hashtag) {
        hashtagRepository.save(hashtag);
    }

    @Override
    public void delete(Long hashtagId) {
        hashtagRepository.deleteById(hashtagId);
    }

    @Override
    public void deleteAllWithReservationArticle(Long postId) {
        hashtagRepository.deleteAllByReservationArticleId(postId);
    }
}
