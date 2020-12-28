package com.github.paulosalonso.research.usecase.answer;

import com.github.paulosalonso.research.domain.AnswerCriteria;
import com.github.paulosalonso.research.domain.ResearchSummary;
import com.github.paulosalonso.research.usecase.port.AnswerPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AnswerRead {

    private final AnswerPort answerPort;

    public ResearchSummary search(AnswerCriteria answerCriteria) {
        return answerPort.search(answerCriteria);
    }
}
