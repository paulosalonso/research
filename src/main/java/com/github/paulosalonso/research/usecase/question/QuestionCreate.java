package com.github.paulosalonso.research.usecase.question;

import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.usecase.port.QuestionPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class QuestionCreate {

    private final QuestionPort questionPort;

    public Question create(UUID researchId, Question question) {
        question = question.toBuilder()
                .id(UUID.randomUUID())
                .build();

        return questionPort.create(researchId, question);
    }
}
