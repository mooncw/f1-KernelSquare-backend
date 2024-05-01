package com.kernelsquare.domainmysql.domain.level.repository;

import com.kernelsquare.domainmysql.domain.level.entity.Level;

import java.util.List;

public interface LevelReader {
    Level findByLevelName(Long levelName);

    Level find(Long levelId);

    Boolean existsLevelOtherThanId(Long levelName, Long levelId);

    List<Level> findAll();
}
