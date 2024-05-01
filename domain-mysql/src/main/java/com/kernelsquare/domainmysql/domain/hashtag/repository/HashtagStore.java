package com.kernelsquare.domainmysql.domain.hashtag.repository;

import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;

public interface HashtagStore {
    void store(Hashtag hashtag);

    void delete(Long hashtagId);

    void deleteAllWithReservationArticle(Long postId);
}
