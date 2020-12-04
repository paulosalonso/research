package com.github.paulosalonso.research.usecase.question;

import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.usecase.port.QuestionPort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class QuestionUpdate {

    private final QuestionPort questionPort;

    public Question update(UUID researchId, Question question) {
        return questionPort.update(researchId, question);
    }
}
