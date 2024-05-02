package com.kernelsquare.domainadmin.domain.notice.repository;

import com.kernelsquare.domainadmin.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findNoticeByNoticeToken(String noticeToken);

    void deleteByNoticeToken(String noticeToken);
}