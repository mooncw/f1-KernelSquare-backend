package com.kernelsquare.domainmysql.domain.question.repository;

import com.kernelsquare.domainmysql.domain.question.entity.Question;

public interface QuestionStore {
    Question store(Question question);

    void delete(Long questionId);
}
