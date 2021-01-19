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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
public class AnswerValidatorTest {

    @InjectMocks
    private AnswerValidator validator;

    @Mock
    private ResearchPort researchPort;

    @Mock
    private QuestionPort questionPort;

    @Mock
    private OptionPort optionPort;

    @Test
    public void givenAnAnswerWhenValidatingThenDoesNotThrowAnyException() {
        var research = buildResearch();
        var answer = buildAnswer(research);

        when(researchPort.read(research.getId())).thenReturn(research);

        assertThatCode(() -> validator.validate(research.getId(), List.of(answer)))
                .doesNotThrowAnyException();

        verify(researchPort).read(research.getId());
        verify(questionPort).read(answer.getResearchId(), answer.getQuestionId());
        verify(optionPort).read(answer.getQuestionId(), answer.getOptionId());
    }

    @Test
    public void givenAnAnswerWithEndsDateInFutureWhenValidatingThenDoesNotThrowAnyException() {
        var research = buildResearch().toBuilder()
                .endsOn(OffsetDateTime.now().plusDays(1))
                .build();
        var answer = buildAnswer(research);

        when(researchPort.read(research.getId())).thenReturn(research);

        assertThatCode(() -> validator.validate(research.getId(), List.of(answer)))
                .doesNotThrowAnyException();

        verify(researchPort).read(research.getId());
        verify(questionPort).read(answer.getResearchId(), answer.getQuestionId());
        verify(optionPort).read(answer.getQuestionId(), answer.getOptionId());
    }

    @Test
    public void givenAnAnswerWhenResearchIsNotFoundThenThrowsException() {
        var research = buildResearch();
        var answer = buildAnswer(research);

        when(researchPort.read(answer.getResearchId())).thenThrow(NotFoundException.class);

        assertThatThrownBy(() -> validator.validate(answer.getResearchId(), List.of(answer)))
                .isExactlyInstanceOf(NotFoundException.class);

        verify(researchPort).read(answer.getResearchId());
        verifyNoInteractions(questionPort);
        verifyNoInteractions(optionPort);
    }

    @Test
    public void givenANotStartedResearchWhenAnswerItThenThrowsException() {
        var research = Research.builder()
                .id(UUID.randomUUID())
                .title("title")
                .startsOn(OffsetDateTime.now().plusMinutes(1))
                .build();

        var answer = buildAnswer(research);

        when(researchPort.read(research.getId())).thenReturn(research);

        assertThatThrownBy(() -> validator.validate(research.getId(), List.of(answer)))
                .isExactlyInstanceOf(InvalidAnswerException.class)
                .hasMessage("Research is not started");

        verify(researchPort).read(research.getId());
        verifyNoInteractions(questionPort);
        verifyNoInteractions(optionPort);
    }

    @Test
    public void givenAFinalizedResearchWhenAnswerItThenThrowsException() {
        var research = Research.builder()
                .id(UUID.randomUUID())
                .title("title")
                .startsOn(OffsetDateTime.now().minusDays(1))
                .endsOn(OffsetDateTime.now().minusMinutes(1))
                .build();

        var answer = buildAnswer(research);

        when(researchPort.read(research.getId())).thenReturn(research);

        assertThatThrownBy(() -> validator.validate(research.getId(), List.of(answer)))
                .isExactlyInstanceOf(InvalidAnswerException.class)
                .hasMessage("Research is finalized");

        verify(researchPort).read(research.getId());
        verifyNoInteractions(questionPort);
        verifyNoInteractions(optionPort);
    }

    @Test
    public void givenAnAnswerWhenQuestionIsNotFoundThenThrowsException() {
        var research = buildResearch();
        var answer = buildAnswer(research);

        when(researchPort.read(research.getId())).thenReturn(research);
        when(questionPort.read(answer.getResearchId(), answer.getQuestionId())).thenThrow(NotFoundException.class);

        assertThatThrownBy(() -> validator.validate(research.getId(), List.of(answer)))
                .isExactlyInstanceOf(InvalidAnswerException.class)
                .hasMessage("Question not found: " + answer.getQuestionId());

        verify(researchPort).read(research.getId());
        verify(questionPort).read(answer.getResearchId(), answer.getQuestionId());
        verifyNoInteractions(optionPort);
    }

    @Test
    public void givenAnAnswerWhenOptionIsNotFoundThenThrowsException() {
        var research = buildResearch();
        var answer = buildAnswer(research);

        when(researchPort.read(research.getId())).thenReturn(research);
        when(optionPort.read(answer.getQuestionId(), answer.getOptionId())).thenThrow(NotFoundException.class);

        assertThatThrownBy(() -> validator.validate(research.getId(), List.of(answer)))
                .isExactlyInstanceOf(InvalidAnswerException.class)
                .hasMessage("Option not found: " + answer.getOptionId());

        verify(researchPort).read(research.getId());
        verify(questionPort).read(answer.getResearchId(), answer.getQuestionId());
        verify(optionPort).read(answer.getQuestionId(), answer.getOptionId());
    }

    @Test
    public void givenAQuestionThatAcceptsMultipleSelectWhenValidateAnAnswerWithMoreThenOneOptionForItThenDoesNotThrowAnyException() {
        var research = buildResearch();

        var questionId = UUID.randomUUID();

        var answerA = Answer.builder()
                .researchId(research.getId())
                .questionId(questionId)
                .optionId(UUID.randomUUID())
                .build();

        var answerB = Answer.builder()
                .researchId(research.getId())
                .questionId(questionId)
                .optionId(UUID.randomUUID())
                .build();

        var question = Question.builder()
                .id(questionId)
                .description("description")
                .multiSelect(true)
                .build();

        when(researchPort.read(research.getId())).thenReturn(research);
        when(questionPort.search(eq(research.getId()), any(QuestionCriteria.class))).thenReturn(List.of(question));

        assertThatCode(() -> validator.validate(research.getId(), List.of(answerA, answerB)))
                .doesNotThrowAnyException();

        verify(researchPort).read(research.getId());
        verify(questionPort).search(eq(research.getId()), any(QuestionCriteria.class));
        verify(questionPort, times(2)).read(research.getId(), questionId);
        verify(optionPort).read(questionId, answerA.getOptionId());
        verify(optionPort).read(questionId, answerB.getOptionId());
    }

    @Test
    public void givenAQuestionThatNotAcceptsMultipleSelectWhenValidateAnAnswerWithOneOptionForItThenDoesNotThrowAnyException() {
        var research = buildResearch();

        var questionId = UUID.randomUUID();

        var answerA = Answer.builder()
                .researchId(research.getId())
                .questionId(questionId)
                .optionId(UUID.randomUUID())
                .build();

        var answerB = Answer.builder()
                .researchId(research.getId())
                .questionId(UUID.randomUUID())
                .optionId(UUID.randomUUID())
                .build();

        var question = Question.builder()
                .id(questionId)
                .description("description")
                .multiSelect(false)
                .build();

        when(researchPort.read(research.getId())).thenReturn(research);
        when(questionPort.search(eq(research.getId()), any(QuestionCriteria.class))).thenReturn(List.of(question));

        assertThatCode(() -> validator.validate(research.getId(), List.of(answerA, answerB)))
                .doesNotThrowAnyException();

        verify(researchPort).read(research.getId());
        verify(questionPort).search(eq(research.getId()), any(QuestionCriteria.class));
        verify(questionPort).read(research.getId(), questionId);
        verify(optionPort).read(answerA.getQuestionId(), answerA.getOptionId());
        verify(optionPort).read(answerB.getQuestionId(), answerB.getOptionId());
    }

    @Test
    public void givenAQuestionThatNotAcceptsMultipleSelectWhenValidateAnAnswerWithMoreThenOneOptionForItThenThrowException() {
        var research = buildResearch();

        var questionId = UUID.randomUUID();

        var answerA = Answer.builder()
                .researchId(research.getId())
                .questionId(questionId)
                .optionId(UUID.randomUUID())
                .build();

        var answerB = Answer.builder()
                .researchId(research.getId())
                .questionId(questionId)
                .optionId(UUID.randomUUID())
                .build();

        var question = Question.builder()
                .id(questionId)
                .description("description")
                .multiSelect(false)
                .build();

        when(researchPort.read(research.getId())).thenReturn(research);
        when(questionPort.search(eq(research.getId()), any(QuestionCriteria.class))).thenReturn(List.of(question));

        assertThatThrownBy(() -> validator.validate(research.getId(), List.of(answerA, answerB)))
                .isExactlyInstanceOf(InvalidAnswerException.class)
                .hasMessage("The question does not allow the selection of various options: " + questionId);

        verify(researchPort).read(research.getId());
        verify(questionPort).search(eq(research.getId()), any(QuestionCriteria.class));
        verify(questionPort, times(2)).read(research.getId(), questionId);
        verify(optionPort).read(questionId, answerA.getOptionId());
        verify(optionPort).read(questionId, answerB.getOptionId());
    }

    private Research buildResearch() {
        return Research.builder()
                .id(UUID.randomUUID())
                .title("title")
                .startsOn(OffsetDateTime.now().minusDays(1))
                .build();
    }

    private Answer buildAnswer(Research research) {
        return Answer.builder()
                .researchId(research.getId())
                .questionId(UUID.randomUUID())
                .optionId(UUID.randomUUID())
                .build();
    }

}
