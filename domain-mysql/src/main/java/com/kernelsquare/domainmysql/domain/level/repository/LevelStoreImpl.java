package com.kernelsquare.domainmysql.domain.level.repository;

import com.kernelsquare.domainmysql.domain.level.entity.Level;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LevelStoreImpl implements LevelStore {
    private final LevelRepository levelRepository;

    @Override
    public void store(Level level) {
        levelRepository.save(level);
    }

    @Override
    public void delete(Long levelId) {
        levelRepository.deleteById(levelId);
    }
}
