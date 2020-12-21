package com.github.paulosalonso.research.usecase.answer;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.usecase.exception.NotFoundException;
import com.github.paulosalonso.research.usecase.port.OptionPort;
import com.github.paulosalonso.research.usecase.port.QuestionPort;
import com.github.paulosalonso.research.usecase.port.ResearchPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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
    public void givenAnAnswerWhenValidatingThenReturnIt() {
        var answer = buildAnswer();

        var validated = validator.validate(answer);

        assertThat(validated).isSameAs(answer);

        verify(researchPort).read(answer.getResearchId());
        verify(questionPort).read(answer.getResearchId(), answer.getQuestionId());
        verify(optionPort).read(answer.getQuestionId(), answer.getOptionId());
    }

    @Test
    public void givenAnAnswerWhenResearchIsNotFoundThenThrowsException() {
        var answer = buildAnswer();

        when(researchPort.read(answer.getResearchId())).thenThrow(NotFoundException.class);

        assertThatThrownBy(() -> validator.validate(answer))
                .isExactlyInstanceOf(InvalidAnswerException.class)
                .hasMessage("Research not found: " + answer.getResearchId());

        verify(researchPort).read(answer.getResearchId());
        verifyNoInteractions(questionPort);
        verifyNoInteractions(optionPort);
    }

    @Test
    public void givenAnAnswerWhenQuestionIsNotFoundThenThrowsException() {
        var answer = buildAnswer();

        when(questionPort.read(answer.getResearchId(), answer.getQuestionId())).thenThrow(NotFoundException.class);

        assertThatThrownBy(() -> validator.validate(answer))
                .isExactlyInstanceOf(InvalidAnswerException.class)
                .hasMessage("Question not found: " + answer.getQuestionId());

        verify(researchPort).read(answer.getResearchId());
        verify(questionPort).read(answer.getResearchId(), answer.getQuestionId());
        verifyNoInteractions(optionPort);
    }

    @Test
    public void givenAnAnswerWhenOptionIsNotFoundThenThrowsException() {
        var answer = buildAnswer();

        when(optionPort.read(answer.getQuestionId(), answer.getOptionId())).thenThrow(NotFoundException.class);

        assertThatThrownBy(() -> validator.validate(answer))
                .isExactlyInstanceOf(InvalidAnswerException.class)
                .hasMessage("Option not found: " + answer.getOptionId());

        verify(researchPort).read(answer.getResearchId());
        verify(questionPort).read(answer.getResearchId(), answer.getQuestionId());
        verify(optionPort).read(answer.getQuestionId(), answer.getOptionId());
    }

    private Answer buildAnswer() {
        return Answer.builder()
                .researchId(UUID.randomUUID())
                .questionId(UUID.randomUUID())
                .optionId(UUID.randomUUID())
                .build();
    }

}
