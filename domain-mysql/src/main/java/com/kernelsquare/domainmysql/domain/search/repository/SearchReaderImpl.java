package com.kernelsquare.domainmysql.domain.search.repository;

import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchReaderImpl implements SearchReader {
    private final SearchRepository searchRepository;

    @Override
    public Page<Question> searchQuestions(Pageable pageable, String keyword) {
        return searchRepository.searchQuestionsByKeyword(pageable, keyword);
    }

    @Override
    public Page<TechStack> searchTechStacks(Pageable pageable, String keyword) {
        return searchRepository.searchTechStacksByKeyword(pageable, keyword);
    }
}
