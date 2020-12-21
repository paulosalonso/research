package com.github.paulosalonso.research.usecase.answer;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import com.github.paulosalonso.research.usecase.port.QuestionPort;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AnswerValidator {

    private final ResearchPort researchPort;
    private final QuestionPort questionPort;
    private final OptionPort optionPort;

    public Answer validate(Answer answer) {
        try {
            researchPort.read(answer.getResearchId());
        } catch (NotFoundException e) {
            throw new InvalidAnswerException("Research not found: " + answer.getResearchId());
        }

        try {
            questionPort.read(answer.getResearchId(), answer.getQuestionId());
        } catch (NotFoundException e) {
            throw new InvalidAnswerException("Question not found: " + answer.getQuestionId());
        }

        try {
            optionPort.read(answer.getQuestionId(), answer.getOptionId());
        } catch (NotFoundException e) {
            throw new InvalidAnswerException("Option not found: " + answer.getOptionId());
        }

        return answer;
    }
}
