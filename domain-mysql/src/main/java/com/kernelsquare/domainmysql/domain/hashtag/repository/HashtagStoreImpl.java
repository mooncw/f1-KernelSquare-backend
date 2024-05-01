package com.kernelsquare.domainmysql.domain.hashtag.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HashtagStoreImpl implements HashtagStore {
    private final HashtagRepository hashtagRepository;

    @Override
    public void delete(Long hashtagId) {
        hashtagRepository.deleteById(hashtagId);
    }

    @Override
    public void deleteAll(Long postId) {
        hashtagRepository.deleteAllByReservationArticleId(postId);
    }
}
