package com.github.paulosalonso.research.usecase.answer;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.usecase.port.AnswerPort;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class AnswerCreate {

    private final AnswerPort answerPort;
    private final AnswerValidator validator;

    public Answer create(Answer answer) {
        validator.validate(answer);
        answer.setDate(OffsetDateTime.now());
        return answerPort.create(answer);
    }
}
