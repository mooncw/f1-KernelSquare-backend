package com.kernelsquare.domainmysql.domain.question_tech_stack.repository;

import com.kernelsquare.domainmysql.domain.question_tech_stack.entity.QuestionTechStack;

public interface QuestionTechStackStore {
    void store(QuestionTechStack questionTechStack);

    void deleteAll(Long questionId);
}
