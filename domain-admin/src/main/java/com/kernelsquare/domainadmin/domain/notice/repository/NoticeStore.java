package com.kernelsquare.domainadmin.domain.notice.repository;

import com.kernelsquare.domainadmin.domain.notice.entity.Notice;

public interface NoticeStore {
	Notice store(Notice initNotice);

	void delete(String noticeToken);
}
