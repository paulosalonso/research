package com.github.paulosalonso.research.usecase.port;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.AnswerCriteria;
import com.github.paulosalonso.research.domain.ResearchSummary;

public interface AnswerPort {
    Answer create(Answer answer);
    ResearchSummary search(AnswerCriteria answerCriteria);
}
