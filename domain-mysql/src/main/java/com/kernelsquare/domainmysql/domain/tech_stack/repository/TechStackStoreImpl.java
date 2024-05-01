package com.kernelsquare.domainmysql.domain.tech_stack.repository;

import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TechStackStoreImpl implements TechStackStore {
    private final TechStackRepository techStackRepository;

    @Override
    public TechStack store(TechStack techStack) {
        return techStackRepository.save(techStack);
    }

    @Override
    public void delete(Long techStackId) {
        techStackRepository.deleteById(techStackId);
    }
}
