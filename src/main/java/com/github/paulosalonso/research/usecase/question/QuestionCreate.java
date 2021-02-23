package com.github.paulosalonso.research.usecase.question;

import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.usecase.port.QuestionPort;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class QuestionCreate {

    private final QuestionPort questionPort;
    private final ResearchPort researchPort;

    public Question create(UUID researchId, String tenant, Question question) {
        question = question.toBuilder()
                .id(UUID.randomUUID())
                .tenant(tenant)
                .sequence(researchPort.getNextQuestionSequence(researchId))
                .build();

        return questionPort.create(researchId, tenant, question);
    }
}
