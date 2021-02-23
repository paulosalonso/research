package com.github.paulosalonso.research.usecase.port;

import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.domain.QuestionCriteria;

import java.util.List;
import java.util.UUID;

public interface QuestionPort {

    Question create(UUID researchId, String tenant, Question question);
    Question read(UUID researchId, UUID questionId);
    Question readFetchingOptions(UUID researchId, UUID questionId);
    List<Question> search(UUID researchId, QuestionCriteria criteria);
    Question update(UUID researchId, Question question);
    void delete(UUID researchId, UUID questionId);
    Integer getNextOptionSequence(UUID questionId);
}
