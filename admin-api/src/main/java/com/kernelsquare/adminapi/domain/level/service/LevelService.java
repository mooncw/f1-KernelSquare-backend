package com.kernelsquare.adminapi.domain.level.service;

import com.kernelsquare.adminapi.domain.level.dto.*;
import com.kernelsquare.core.common_response.error.code.LevelErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelReader;
import com.kernelsquare.domainmysql.domain.level.repository.LevelStore;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LevelService {
	private final LevelReader levelReader;
	private final LevelStore levelStore;

	@Transactional
	public CreateLevelResponse createLevel(CreateLevelRequest createLevelRequest) {
		Level level = CreateLevelRequest.toEntity(createLevelRequest);
		try {
			levelStore.store(level);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(LevelErrorCode.LEVEL_ALREADY_EXISTED);
		}
		return CreateLevelResponse.from(level);
	}

	@Transactional(readOnly = true)
	public FindAllLevelResponse findAllLevel() {
		List<Level> levelList = levelReader.findAll();
		return FindAllLevelResponse.from(levelList);
	}

	@Transactional
	public void deleteLevel(Long levelId) {
		levelStore.delete(levelId);
	}

	@Transactional
	public UpdateLevelResponse updateLevel(Long levelId, UpdateLevelRequest updateLevelRequest) {
		Level level = levelReader.find(levelId);

		if (levelReader.existsLevelOtherThanId(updateLevelRequest.name(), levelId)) {
			throw new BusinessException(LevelErrorCode.LEVEL_ALREADY_EXISTED);
		}

		level.update(updateLevelRequest.name(), updateLevelRequest.imageUrl(), updateLevelRequest.levelUpperLimit());

		return UpdateLevelResponse.from(level);
	}

}
