package com.kernelsquare.domainmysql.domain.tech_stack.repository;

import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;

public interface TechStackStore {
    TechStack store(TechStack techStack);

    void delete(Long techStackId);
}
