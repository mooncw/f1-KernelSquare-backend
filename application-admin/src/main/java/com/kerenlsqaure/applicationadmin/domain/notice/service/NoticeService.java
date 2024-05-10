package com.kerenlsqaure.applicationadmin.domain.notice.service;

import com.kerenlsqaure.applicationadmin.domain.notice.command.NoticeCommand;
import com.kerenlsqaure.applicationadmin.domain.notice.info.NoticeInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {
	NoticeInfo createNotice(NoticeCommand.CreateCommand command);

	NoticeInfo updateNotice(NoticeCommand.UpdateCommand command, String noticeToken);

	NoticeInfo changeGeneral(String noticeToken);

	NoticeInfo changeQNA(String noticeToken);

	NoticeInfo changeCoffeeChat(String noticeToken);

	void deleteNotice(String noticeToken);

	NoticeInfo findNotice(String noticeToken);

	Page<NoticeInfo> findAllNotice(Pageable pageable);
}
