package com.kernelsquare.memberapi.domain.hashtag.service;

import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;
import com.kernelsquare.domainmysql.domain.hashtag.repository.HashtagReader;
import com.kernelsquare.domainmysql.domain.hashtag.repository.HashtagStore;
import com.kernelsquare.memberapi.domain.hashtag.dto.FindAllHashtagResponse;
import com.kernelsquare.memberapi.domain.hashtag.dto.FindHashtagResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HashtagService {
	private final HashtagReader hashtagReader;
	private final HashtagStore hashtagStore;

	@Transactional(readOnly = true)
	public FindAllHashtagResponse findAllHashtag() {

		List<FindHashtagResponse> result = new ArrayList<>();

		List<Hashtag> hashtagList = hashtagReader.findAll();
		for (Hashtag hashtag : hashtagList) {
			result.add(FindHashtagResponse.from(hashtag));
		}
		return FindAllHashtagResponse.from(result);
	}

	@Transactional
	public void deleteHashtag(Long hashtagId) {
		hashtagStore.delete(hashtagId);
	}
}
