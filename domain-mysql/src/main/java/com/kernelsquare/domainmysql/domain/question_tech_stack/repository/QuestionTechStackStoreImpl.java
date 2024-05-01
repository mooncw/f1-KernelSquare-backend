package com.kernelsquare.domainmysql.domain.question_tech_stack.repository;

import com.kernelsquare.domainmysql.domain.question_tech_stack.entity.QuestionTechStack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionTechStackStoreImpl implements QuestionTechStackStore {
    private final QuestionTechStackRepository questionTechStackRepository;

    @Override
    public void store(QuestionTechStack questionTechStack) {
        questionTechStackRepository.save(questionTechStack);
    }

    @Override
    public void deleteAll(Long questionId) {
        questionTechStackRepository.deleteAllByQuestionId(questionId);
    }
}
