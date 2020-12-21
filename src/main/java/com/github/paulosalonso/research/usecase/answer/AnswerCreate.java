package com.github.paulosalonso.research.usecase.answer;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.usecase.port.AnswerPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AnswerCreate {

    private final AnswerPort answerPort;
    private final AnswerValidator validator;

    public Answer create(Answer answer) {
        return answerPort.create(validator.validate(answer));
    }
}
