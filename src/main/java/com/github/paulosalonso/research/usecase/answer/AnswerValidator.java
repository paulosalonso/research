package com.github.paulosalonso.research.usecase.answer;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.domain.Question;
import com.github.paulosalonso.research.domain.QuestionCriteria;
import com.github.paulosalonso.research.domain.Research;
import com.github.paulosalonso.research.usecase.exception.InvalidAnswerException;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import com.github.paulosalonso.research.usecase.port.QuestionPort;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class AnswerValidator {

    private final ResearchPort researchPort;
    private final QuestionPort questionPort;
    private final OptionPort optionPort;

    public void validate(UUID researchId, String tenant, List<Answer> answers) {
        validate(researchPort.read(researchId, tenant), answers);
    }

    private void validate(Research research, List<Answer> answers) {
        validateResearchDatetimeRange(research);
        var questions = questionPort.search(research.getId(), QuestionCriteria.builder().build());
        validateThatAllQuestionsHasBeenAnswered(questions, answers);
    }

    private void validateResearchDatetimeRange(Research research) {
        if (research.getStartsOn().isAfter(OffsetDateTime.now())) {
            throw new InvalidAnswerException("Research is not started");
        }

        var finalized = ofNullable(research.getEndsOn())
                .map(endsOn -> endsOn.isBefore(OffsetDateTime.now()))
                .orElse(false);

        if (finalized) {
            throw new InvalidAnswerException("Research is finalized");
        }
    }

    private void validateThatAllQuestionsHasBeenAnswered(List<Question> questions, List<Answer> answers) {
        var answeredQuestions = answers.stream()
                .peek(this::validateDataExistence)
                .map(Answer::getQuestionId)
                .collect(toList());

        var notAnsweredQuestions = questions.stream()
                .peek(question -> validateMultipleOptionsSelection(question, answers))
                .map(Question::getId)
                .filter(not(answeredQuestions::contains))
                .map(UUID::toString)
                .collect(toList());

        if (!notAnsweredQuestions.isEmpty()) {
            throw new InvalidAnswerException("The follow questions have not been answered: " + String.join(", ", notAnsweredQuestions));
        }
    }

    private void validateDataExistence(Answer answer) {
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

    private void validateMultipleOptionsSelection(Question question, List<Answer> answers) {
        var answersCount = answers.stream()
                .filter(answer -> answer.getQuestionId().equals(question.getId()))
                .count();

        if (answersCount > 1 && !question.getMultiSelect()) {
            throw new InvalidAnswerException(
                    "The question does not allow the selection of various options: " + question.getId());
        }
    }
}
