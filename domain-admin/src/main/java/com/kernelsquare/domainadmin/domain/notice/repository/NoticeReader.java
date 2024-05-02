package com.kernelsquare.domainadmin.domain.notice.repository;

import com.kernelsquare.domainadmin.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeReader {
	Notice findNotice(String NoticeToken);

	Page<Notice> findAllNotice(Pageable pageable);
}
