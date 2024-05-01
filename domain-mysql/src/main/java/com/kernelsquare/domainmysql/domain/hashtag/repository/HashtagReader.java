package com.kernelsquare.domainmysql.domain.hashtag.repository;

import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;

import java.util.List;

public interface HashtagReader {
    List<Hashtag> findAll(Long articleId);

    Long count(Long articleId);
}