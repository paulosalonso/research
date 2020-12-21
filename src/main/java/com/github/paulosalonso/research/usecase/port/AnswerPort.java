package com.github.paulosalonso.research.usecase.port;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.AnswerCriteria;

import java.util.List;

public interface AnswerPort {
    Answer create(Answer answer);
    List<Answer> search(AnswerCriteria answerCriteria);
}
