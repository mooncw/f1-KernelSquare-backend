package com.kernelsquare.domainmysql.domain.tech_stack.repository;

import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;

public interface TechStackReader {
    TechStack find(String skill);

    Boolean exists(String skill);
}
