package com.kernelsquare.core.util;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
	@CreatedDate
	@Column(nullable = false, name = "created_date", columnDefinition = "datetime", updatable = false)
	private LocalDateTime createdDate;

	@LastModifiedDate
	@Column(name = "modified_date", columnDefinition = "datetime")
	private LocalDateTime modifiedDate;
}
