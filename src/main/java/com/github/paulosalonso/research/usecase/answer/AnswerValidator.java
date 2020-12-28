package com.github.paulosalonso.research.usecase.answer;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.domain.QuestionCriteria;
import com.github.paulosalonso.research.usecase.exception.InvalidAnswerException;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import com.github.paulosalonso.research.usecase.port.QuestionPort;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class AnswerValidator {

    private final ResearchPort researchPort;
    private final QuestionPort questionPort;
    private final OptionPort optionPort;

    public void validate(UUID researchId, List<Answer> answers) {
        var questions = questionPort.search(researchId, QuestionCriteria.builder().build());

        var answeredQuestions = answers.stream()
                .peek(this::validate)
                .map(Answer::getQuestionId)
                .collect(toList());

        var notAnsweredQuestions = questions.stream()
                .map(Question::getId)
                .filter(not(answeredQuestions::contains))
                .map(UUID::toString)
                .collect(toList());

        if (!notAnsweredQuestions.isEmpty()) {
            throw new InvalidAnswerException("The follow questions have not been answered: " + String.join(", ", notAnsweredQuestions));
        }
    }

    private void validate(Answer answer) {
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
    }
}
