package com.github.paulosalonso.research.usecase.answer;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.usecase.port.AnswerPort;
import com.github.paulosalonso.research.usecase.port.NotifierPort;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AnswerCreate {

    private final AnswerPort answerPort;
    private final AnswerValidator validator;
    private final OptionPort optionPort;
    private final NotifierPort notifierPort;

    public void create(UUID researchId, String tenant, List<Answer> answers) {
        validator.validate(researchId, tenant, answers);

        answers.stream()
                .peek(answer -> answer.setDate(OffsetDateTime.now()))
                .forEach(answerPort::create);

        answers.forEach(this::notify);
    }

    private void notify(Answer answer) {
        if (optionPort.shouldNotify(answer.getOptionId())) {
            notifierPort.notifyAnswer(answer);
        }
    }
}
