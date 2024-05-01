package com.kernelsquare.domainmysql.domain.question.repository;

import com.kernelsquare.domainmysql.domain.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionStoreImpl implements QuestionStore {
    private final QuestionRepository questionRepository;

    @Override
    public Question store(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public void delete(Long questionId) {
        questionRepository.deleteById(questionId);
    }
}
