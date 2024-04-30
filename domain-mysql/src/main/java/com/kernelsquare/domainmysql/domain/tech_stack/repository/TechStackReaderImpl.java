package com.kernelsquare.domainmysql.domain.tech_stack.repository;

import com.kernelsquare.core.common_response.error.code.TechStackErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TechStackReaderImpl implements TechStackReader {
    private final TechStackRepository techStackRepository;

    @Override
    public TechStack find(String skill) {
        return techStackRepository.findBySkill(skill)
            .orElseThrow(() -> new BusinessException(TechStackErrorCode.TECH_STACK_NOT_FOUND));
    }

    @Override
    public Boolean exists(String skill) {
        return techStackRepository.existsBySkill(skill);
    }
}
