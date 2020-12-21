package com.github.paulosalonso.research.usecase.answer;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.AnswerCriteria;
import com.github.paulosalonso.research.usecase.port.AnswerPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AnswerRead {

    private final AnswerPort answerPort;

    public List<Answer> search(AnswerCriteria answerCriteria) {
        return answerPort.search(answerCriteria);
    }
}
