package com.github.paulosalonso.research.usecase.answer;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.usecase.exception.InvalidAnswerException;
import com.github.paulosalonso.research.usecase.port.AnswerPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnswerCreateTest {

    @InjectMocks
    private AnswerCreate answerCreate;

    @Mock
    private AnswerPort port;

    @Mock
    private AnswerValidator validator;

    @Test
    public void givenAnAnswerWhenCreateThenCallPort() {
        var date = OffsetDateTime.now();

        var toSave = Answer.builder()
                .date(date)
                .researchId(UUID.randomUUID())
                .questionId(UUID.randomUUID())
                .optionId(UUID.randomUUID())
                .build();

        when(validator.validate(toSave)).thenReturn(toSave);

        answerCreate.create(toSave);

        ArgumentCaptor<Answer> answerCaptor = ArgumentCaptor.forClass(Answer.class);
        verify(port).create(answerCaptor.capture());

        var saved = answerCaptor.getValue();
        assertThat(saved.getDate()).isEqualTo(date);
        assertThat(saved.getResearchId()).isEqualTo(toSave.getResearchId());
        assertThat(saved.getQuestionId()).isEqualTo(toSave.getQuestionId());
        assertThat(saved.getOptionId()).isEqualTo(toSave.getOptionId());

        verify(validator).validate(toSave);
    }

    @Test
    public void givenAnAnswerWhenValidationThrowsExceptionThenRethrowsIt() {
        var answer = Answer.builder().build();
        var exception = new InvalidAnswerException("test exception");

        when(validator.validate(answer)).thenThrow(exception);

        assertThatThrownBy(() -> answerCreate.create(answer))
                .isSameAs(exception);

        verify(validator).validate(answer);
    }
}
