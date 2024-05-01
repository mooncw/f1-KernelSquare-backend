package com.kernelsquare.domainmysql.domain.tech_stack.repository;

import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;

import java.util.List;

public interface TechStackReader {
    TechStack findBySkill(String skill);

    TechStack find(Long techStackId);

    List<TechStack> findAll();

    Boolean existsBySkill(String skill);
}
