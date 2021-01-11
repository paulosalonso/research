package com.github.paulosalonso.research.usecase.question;

import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.domain.QuestionCriteria;
import com.github.paulosalonso.research.usecase.port.QuestionPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class QuestionRead {

    private final QuestionPort questionPort;

    public Question read(UUID researchId, UUID questionId, boolean fillOptions) {
        if (fillOptions) {
            return questionPort.readFetchingOptions(researchId, questionId);
        }

        return questionPort.read(researchId, questionId);
    }

    public List<Question> search(UUID researchId, QuestionCriteria criteria) {
        return questionPort.search(researchId, criteria);
    }
}
