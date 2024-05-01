package com.kernelsquare.domainmysql.domain.level.repository;

import com.kernelsquare.domainmysql.domain.level.entity.Level;

public interface LevelStore {
    void store(Level level);
}
