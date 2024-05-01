package com.kernelsquare.domainmysql.domain.level.repository;

import com.kernelsquare.core.common_response.error.code.LevelErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LevelReaderImpl implements LevelReader {
    private final LevelRepository levelRepository;

    @Override
    public Level findByLevelName(Long name) {
        return levelRepository.findByName(name)
            .orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));
    }

    @Override
    public Level find(Long levelId) {
        return levelRepository.findById(levelId)
            .orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));
    }

    @Override
    public Boolean existsLevelOtherThanId(Long levelName, Long levelId) {
        return levelRepository.existsByNameAndIdNot(levelName, levelId);
    }

    @Override
    public List<Level> findAll() {
        return levelRepository.findAll();
    }
}
