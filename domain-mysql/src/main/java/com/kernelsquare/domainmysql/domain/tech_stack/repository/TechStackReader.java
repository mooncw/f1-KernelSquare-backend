package com.kernelsquare.domainmysql.domain.tech_stack.repository;

import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TechStackReader {
    TechStack findBySkill(String skill);

    TechStack find(Long techStackId);

    List<TechStack> findAll();

    Boolean existsBySkill(String skill);

    Page<TechStack> findAllPage(Pageable pageable);
}
