package com.github.paulosalonso.research.usecase.answer;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.usecase.port.AnswerPort;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AnswerCreate {

    private final AnswerPort answerPort;
    private final AnswerValidator validator;

    public void create(UUID researchId, List<Answer> answers) {
        validator.validate(researchId, answers);

        answers.stream()
                .peek(answer -> answer.setDate(OffsetDateTime.now()))
                .forEach(answerPort::create);
    }
}
