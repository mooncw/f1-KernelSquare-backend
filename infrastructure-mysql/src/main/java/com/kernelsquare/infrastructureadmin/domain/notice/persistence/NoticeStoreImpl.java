package com.kernelsquare.infrastructureadmin.domain.notice.persistence;

import com.kernelsquare.domainadmin.domain.notice.entity.Notice;
import com.kernelsquare.domainadmin.domain.notice.repository.NoticeRepository;
import com.kernelsquare.domainadmin.domain.notice.repository.NoticeStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoticeStoreImpl implements NoticeStore {
	private final NoticeRepository noticeRepository;

	@Override
	public Notice store(Notice initNotice) {
		return noticeRepository.save(initNotice);
	}

	@Override
	public void delete(String noticeToken) {
		noticeRepository.deleteByNoticeToken(noticeToken);
	}
}
