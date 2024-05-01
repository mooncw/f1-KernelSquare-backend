package com.kernelsquare.domainmysql.domain.search.repository;

import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchReader {
    Page<Question> searchQuestions(Pageable pageable, String keyword);

    Page<TechStack> searchTechStacks(Pageable pageable, String keyword);
}
